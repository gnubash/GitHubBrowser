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
import com.antondevs.apps.githubbrowser.utilities.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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

    private String basicCredentials;

    private UserEntry currentUser;

    private RepoEntry currentRepo;

    private ResponsePaging<List<UserEntry>> userSearchHelper;

    private List<UserEntry> currentSearchResults;

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
            Log.d(LOGTAG, "databaseHelper.getStoredAuth() = " + databaseHelper.getStoredAuth());
            listener.onAuthenticationRequered();
        }
        else {
            AuthEntry entry = databaseHelper.getAuthentication();
            performAuthentication(entry.getLogin(), entry.getPass(), listener);
        }
    }

    @Override
    public void performAuthentication(final String username, final String password, final AuthenticationListener listener) {
        basicCredentials = Credentials.basic(username, password, UTF_8);
        APIService.setCredentials(basicCredentials);

        userHelper = new UserWrapperHelper(username);

        Observable<UserEntry> createUserEntry = userHelper.createUser();
        Observer<UserEntry> createUserEntryObserver = new Observer<UserEntry>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "performAuthentication.onSubscribe() " + d.isDisposed());
            }

            @Override
            public void onNext(UserEntry userEntry) {
                Log.d(LOGTAG, "performAuthentication.onNext() ");
                currentUser = userEntry;
                loadedUsers.put(currentUser.getLogin(), currentUser);
                databaseHelper.writeAuthnetication(new AuthEntry(username, password));
                databaseHelper.writeUser(currentUser);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "performAuthentication.onError() ");
                if (e instanceof IOException) {
                    listener.onNetworkConnectionFailure();
                    return;
                }
                e.printStackTrace();
                listener.onAuthenticationFailed();
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "performAuthentication.onComplete()");
                listener.onUserAuthenticated();
            }
        };

        createUserEntry.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(createUserEntryObserver);

    }

    @Override
    public void queryUser(final UserListener listener, final String loginName) {

        UserWrapper userWrapper = new UserWrapperHelper(loginName);

        Observable<UserEntry> userEntryObservable = userWrapper.createUser();

        Observer<UserEntry> userEntryObserver = new Observer<UserEntry>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "queryUser.onSubscribe() " + d.isDisposed());
            }

            @Override
            public void onNext(UserEntry userEntry) {
                Log.d(LOGTAG, "queryUser.onNext()");
                currentUser = userEntry;
                loadedUsers.put(userEntry.getLogin(), userEntry);
                databaseHelper.writeUser(userEntry);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "queryUser.onError()");
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "queryUser.onComplete()");
                listener.onUserLoaded(currentUser);
            }
        };

        userEntryObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userEntryObserver);

    }

    @Override
    public void queryUsers(final SearchListener listener, String loginName) {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("q", loginName);

        Observable<List<UserEntry>> observable = userSearchHelper.search(Constants.URL_GIT_API_SEARCH, queryMap);

        Observer<List<UserEntry>> observer = new Observer<List<UserEntry>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "queryUsers.onSubscribe");
            }

            @Override
            public void onNext(List<UserEntry> userEntries) {
                Log.d(LOGTAG, "queryUsers.onNext");
                currentSearchResults = userEntries;
                listener.onSearchSuccess(userEntries);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "queryUsers.onError");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "queryUsers.onComplete");
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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
    public void queryFollowers(final SearchListener listener, String loginName) {

        setCurrentUser(loginName);
        clearSearchCache();

        Observable<List<UserEntry>> observable = userSearchHelper.search(currentUser.getFollowers_url(),
                new HashMap<String, String>());

        Observer<List<UserEntry>> observer = new Observer<List<UserEntry>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "queryFollowers.onSubscribe");
            }

            @Override
            public void onNext(List<UserEntry> userEntries) {
                Log.d(LOGTAG, "queryFollowers.onNext");
                currentSearchResults = userEntries;
                listener.onSearchSuccess(userEntries);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "queryFollowers.onError");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "queryFollowers.onComplete");
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void queryFollowing(final SearchListener listener, String loginName) {

        setCurrentUser(loginName);
        clearSearchCache();

        Log.d(LOGTAG, "following_url before replacement " + currentUser.getFollowing_url());
        String followingUrl = currentUser.getFollowing_url().replace("{/other_user}", "");
        Log.d(LOGTAG, "following_url after replacement " + followingUrl);
        Observable<List<UserEntry>> observable = userSearchHelper.search(followingUrl, new HashMap<String, String>());

        Observer<List<UserEntry>> observer = new Observer<List<UserEntry>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "queryFollowing.onSubscribe");
            }

            @Override
            public void onNext(List<UserEntry> userEntries) {
                Log.d(LOGTAG, "queryFollowing.onNext");
                currentSearchResults = userEntries;
                listener.onSearchSuccess(userEntries);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "queryFollowing.onError");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "queryFollowing.onComplete");
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void queryContributors(final SearchListener listener, String repoName) {

        currentRepo = loadedRepos.get(repoName);
        clearSearchCache();

        Observable<List<UserEntry>> observable = userSearchHelper.search(currentRepo.getContributors_url(),
                new HashMap<String, String>());

        Observer<List<UserEntry>> observer = new Observer<List<UserEntry>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "queryContributors.onSubscribe");
            }

            @Override
            public void onNext(List<UserEntry> userEntries) {
                Log.d(LOGTAG, "queryContributors.onNext");
                currentSearchResults = userEntries;
                listener.onSearchSuccess(userEntries);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "queryContributors.onError");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "queryContributors.onComplete");
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void loadMoreSearchResults(final SearchListener listener) {
        if (userSearchHelper.hasMorePages() && !isLoadingSearchResults) {
            isLoadingSearchResults = true;
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
                            currentSearchResults.addAll(userEntries);
                            listener.onSearchSuccess(currentSearchResults);
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
                                listener.onNoMoreResults();
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

    private void clearSearchCache() {
        currentSearchResults = null;
    }

}
