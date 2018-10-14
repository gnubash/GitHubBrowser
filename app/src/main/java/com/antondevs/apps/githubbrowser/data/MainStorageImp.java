package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;
import com.antondevs.apps.githubbrowser.data.remote.ResponsePaging;
import com.antondevs.apps.githubbrowser.data.remote.UserWrapper;
import com.antondevs.apps.githubbrowser.ui.search.SearchModel;
import com.antondevs.apps.githubbrowser.utilities.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Anton.
 */
public class MainStorageImp implements MainStorage {

    private static final String LOGTAG = MainStorageImp.class.getSimpleName();

    private static final MainStorage mainStorageUniqueInstance = new MainStorageImp();

    private RemoteAPIService apiService;

    private DatabaseHelper databaseHelper;

    private UserEntry currentUser;

    private RepoEntry currentRepo;

    private ResponsePaging<List<UserEntry>> userSearchHelper;

    private SearchModel lastSearchModel;

    private Map<String, UserEntry> loadedUsers;

    private Map<String, RepoEntry> loadedRepos;

    private UserWrapper userHelper;

    private boolean isLoadingSearchResults;

    private MainStorageImp() {
        apiService = APIService.getService();
        loadedRepos = new HashMap<>();
        userSearchHelper = new UserSearchPagingHelper();
        loadedUsers = new HashMap<>();
    }

    public static MainStorage getInstance() {
        return mainStorageUniqueInstance;
    }

    @Override
    public String getLoggedUser() {
        return databaseHelper.getAuthentication().getLogin();
    }

    @Override
    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void checkCredentials(AuthenticationListener listener) {
        if (databaseHelper.getStoredAuth() != 1) {
            Log.d(LOGTAG, "databaseHelper.getStoredAuth = " + databaseHelper.getStoredAuth());
            listener.onAuthenticationRequered();
        }
        else {
            AuthEntry entry = databaseHelper.getAuthentication();
            String basicCredentials = Credentials.basic(entry.getLogin(), entry.getPass(), UTF_8);
            APIService.setCredentials(basicCredentials);
            listener.onUserAuthenticated();
        }
    }

    @Override
    public void performAuthentication(final String username, final String password, final AuthenticationListener listener) {
        String basicCredentials = Credentials.basic(username, password, UTF_8);
        APIService.setCredentials(basicCredentials);
        databaseHelper.writeAuthnetication(new AuthEntry(username, password));

        apiService.authenticated(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "performAuthentication.onSubscribe " + d.isDisposed());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOGTAG, "performAuthentication.onComplete");
                        listener.onUserAuthenticated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "performAuthentication.onError ");
                        if (e instanceof IOException) {
                            listener.onNetworkConnectionFailure();
                            return;
                        }
                        e.printStackTrace();
                        listener.onAuthenticationFailed();
                    }
                });
    }

    @Override
    public void queryUser(final UserListener listener, final String loginName) {

        userHelper = new UserWrapperHelper(loginName);

        Observable<UserEntry> userEntryObservable = userHelper.createUser();

        Observer<UserEntry> userEntryObserver = new Observer<UserEntry>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "queryUser.onSubscribe " + d.isDisposed());
            }

            @Override
            public void onNext(UserEntry userEntry) {
                Log.d(LOGTAG, "queryUser.onNext");
                currentUser = userEntry;
                loadedUsers.put(userEntry.getLogin(), userEntry);
                databaseHelper.writeUser(userEntry);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "queryUser.onError");
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "queryUser.onComplete");
                listener.onUserLoaded(currentUser);
            }
        };

        userEntryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userEntryObserver);

    }

    @Override
    public void loadMoreOwnedRepos(final UserListener listener, String loginName) {

        if (!loginName.equals(currentUser.getLogin())) {
            setCurrentUser(loginName);
            userHelper = new UserWrapperHelper(currentUser);
        }

        Observable<UserEntry> userEntryObservable = userHelper.loadMoreOwnedRepos();

        Observer<UserEntry> userEntryObserver = new Observer<UserEntry>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "loadMoreOwnedRepos.onSubscribe " + d.isDisposed());
            }

            @Override
            public void onNext(UserEntry userEntry) {
                Log.d(LOGTAG, "loadMoreOwnedRepos.onNext");
                currentUser = userEntry;
                Log.d(LOGTAG, "loadMoreOwnedRepos.onNext " + userEntry.toString());
                loadedUsers.put(userEntry.getLogin(), userEntry);
                databaseHelper.writeUser(userEntry);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "loadMoreOwnedRepos.onError");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "loadMoreOwnedRepos.onComplete");
                listener.onUserLoaded(currentUser);
            }
        };

        userEntryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userEntryObserver);
    }

    @Override
    public void loadMoreStarredRepos(final UserListener listener, String loginName) {

        if (!loginName.equals(currentUser.getLogin())) {
            setCurrentUser(loginName);
            userHelper = new UserWrapperHelper(currentUser);
        }

        Observable<UserEntry> userEntryObservable = userHelper.loadMoreStarredRepos();

        Observer<UserEntry> userEntryObserver = new Observer<UserEntry>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "loadMoreStarredRepos.onSubscribe " + d.isDisposed());
            }

            @Override
            public void onNext(UserEntry userEntry) {
                Log.d(LOGTAG, "loadMoreStarredRepos.onNext");
                currentUser = userEntry;
                Log.d(LOGTAG, "loadMoreStarredRepos.onNext " + userEntry.toString());
                loadedUsers.put(userEntry.getLogin(), userEntry);
                databaseHelper.writeUser(userEntry);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "loadMoreStarredRepos.onError");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "loadMoreStarredRepos.onComplete");
                listener.onUserLoaded(currentUser);
            }
        };

        userEntryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userEntryObserver);
    }

    @Override
    public void queryUsers(final SearchListener listener, SearchModel model) {

        initiateNewSearch(listener, model);
    }

    @Override
    public void queryRepo(final RepoListener listener, final String repoFullName) {

        Log.d(LOGTAG, "queryRepo()");

        if (loadedRepos.containsKey(repoFullName)) {
            currentRepo = loadedRepos.get(repoFullName);
            listener.onRepoLoaded(currentRepo);
            return;
        }

        Observable<RepoEntry> repoEntryObservable = apiService.queryRepo(repoFullName);

        Observable<Integer> contributors = RepoBuilder.getRepoContributorsCount(apiService, repoFullName);
        Observable<Integer> commits = RepoBuilder.getRepoCommitsCount(apiService, repoFullName);
        Observable<Integer> releases = RepoBuilder.getRepoReleasesCount(apiService, repoFullName);
        Observable<Integer> branches = RepoBuilder.getRepoBranchesCount(apiService, repoFullName);

        final Observable<RepoEntry> zippedObservable = Observable.zip(contributors, commits, releases, branches,
                new Function4<Integer, Integer, Integer, Integer, RepoEntry>() {
            @Override
            public RepoEntry apply(Integer integer, Integer integer2, Integer integer3, Integer integer4) throws Exception {
                Log.d(LOGTAG, "queryRepo().Function4().apply()");
                currentRepo.setContributors_count(integer);
                currentRepo.setCommits_count(integer2);
                currentRepo.setReleases_count(integer3);
                currentRepo.setBranches_count(integer4);
                loadedRepos.put(currentRepo.getFull_name(), currentRepo);
                return currentRepo;
            }
        });

        Observer<RepoEntry> observer = new Observer<RepoEntry>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "queryRepo.onSubscribe");
            }

            @Override
            public void onNext(RepoEntry repoEntry) {
                Log.d(LOGTAG, "queryRepo.onNext");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "queryRepo.onError");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                listener.onRepoLoaded(currentRepo);
                Log.d(LOGTAG, "queryRepo.onComplete");
            }
        };

        repoEntryObservable.doOnNext(new Consumer<RepoEntry>() {
            @Override
            public void accept(RepoEntry repoEntry) throws Exception {
                currentRepo = repoEntry;
            }
        }).concatWith(Observable.defer(new Callable<ObservableSource<? extends RepoEntry>>() {
            @Override
            public ObservableSource<? extends RepoEntry> call() throws Exception {
                return zippedObservable;
            }
        })).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override
    public void loadMoreSearchResults(final SearchListener listener, SearchModel model) {

        if (isLoadingSearchResults) {
            return;
        }

        isLoadingSearchResults = true;

        if (lastSearchModel != null && !(lastSearchModel.equals(model))) {
            Log.d(LOGTAG, "loadMoreSearchResults.if");

            initiateNewSearch(listener, model);
            return;
        }

        if (userSearchHelper.hasMorePages()) {
            userSearchHelper.getNextPage().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<UserEntry>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(LOGTAG, "loadMoreSearchResults.onSubscribe");
                        }

                        @Override
                        public void onNext(List<UserEntry> userEntries) {
                            Log.d(LOGTAG, "loadMoreSearchResults.onNext");
                            listener.onSearchSuccess(userEntries);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(LOGTAG, "loadMoreSearchResults.onError");
                            isLoadingSearchResults = false;
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            Log.d(LOGTAG, "loadMoreSearchResults.onComplete");
                            if (!userSearchHelper.hasMorePages()) {
                                Log.d(LOGTAG, "loadMoreSearchResults.onComplete.if");
                            }
                            isLoadingSearchResults = false;
                        }
                    });
        }

    }

    private void setCurrentUser(String loginName) {
        if (currentUser == null) {
            return;
        }

        if (!currentUser.getLogin().equals(loginName) && loadedUsers.containsKey(loginName)) {
            Log.d(LOGTAG, "setCurrentUser.if");
            currentUser = loadedUsers.get(loginName);
        }
    }

    private void initiateNewSearch(final SearchListener listener, final SearchModel model) {
        Log.d(LOGTAG, "initiateNewSearch");
        Map<String, String> newSearchMap = new HashMap<>();
        String url = "";
        switch (model.getSearchType()) {
            case USER:
                url = Constants.URL_GIT_API_USER_SEARCH;
                newSearchMap.put("q", model.getSearchCriteria());
                break;
            case FOLLOWERS:
                url = loadedUsers.get(model.getSearchCriteria()).getFollowers_url();
                break;
            case FOLLOWING:
                url = loadedUsers.get(model.getSearchCriteria()).getFollowing_url();
                break;
            case CONTRIBUTORS:
                url = loadedRepos.get(model.getSearchCriteria()).getContributors_url();
                break;
        }

        int nextPage = model.getCurrentResultsCount() / Constants.SEARCH_QUERIES_MAX_PER_PAGE + 1;
        String requestPageNumber = String.valueOf(nextPage);
        Log.d(LOGTAG, "initiateNewSearch requestPageNumber = " + requestPageNumber);
        newSearchMap.put("page", requestPageNumber);

        userSearchHelper.search(url, newSearchMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserEntry>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "initiateNewSearch.onSubscribe");
                    }

                    @Override
                    public void onNext(List<UserEntry> userEntries) {
                        Log.d(LOGTAG, "initiateNewSearch.onNext");
                        listener.onSearchSuccess(userEntries);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "initiateNewSearch.onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOGTAG, "initiateNewSearch.onComplete");
                        lastSearchModel = model;
                    }
                });
    }

}
