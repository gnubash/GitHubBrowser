package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.cache.CacheStorage;
import com.antondevs.apps.githubbrowser.data.cache.LocalCache;
import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.data.database.model.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;
import com.antondevs.apps.githubbrowser.data.remote.RepoBuilder;
import com.antondevs.apps.githubbrowser.data.remote.UserWrapper;
import com.antondevs.apps.githubbrowser.data.remote.UserWrapperHelper;
import com.antondevs.apps.githubbrowser.data.remote.ResponsePaging;
import com.antondevs.apps.githubbrowser.data.remote.UserSearchPagingHelper;
import com.antondevs.apps.githubbrowser.ui.search.SearchModel;
import com.antondevs.apps.githubbrowser.ui.search.SearchType;
import com.antondevs.apps.githubbrowser.utilities.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function5;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.ResponseBody;

/**
 * Created by Anton.
 */
public class MainStorageImp implements MainStorage {

    private static final String LOGTAG = MainStorageImp.class.getSimpleName();
    private static final MainStorage instance = new MainStorageImp();

    private GitHubBrowserDatabase database;
    private ResponsePaging<List<UserEntry>> userSearchHelper;
    private SearchModel lastSearch;

    private RemoteAPIService apiService;
    private LocalCache cache;

    private String loggedUser;

    private MainStorageImp() {
        Log.d(LOGTAG, "MainStorageImp");
        apiService = APIService.getService();
        userSearchHelper = new UserSearchPagingHelper();
        cache = CacheStorage.getInstance();
    }

    public static MainStorage getInstance() {
        return instance;
    }

    @Override
    public void setDatabaseHelper(GitHubBrowserDatabase appDatabase) {
        Log.d(LOGTAG, "setDatabaseHelper");
        database = appDatabase;

    }

    @Override
    public String getLoggedUser() {
        return loggedUser;
    }

    @Override
    public Single<UserEntry> logIn() {

        return database.authDao().getAuth().subscribeOn(Schedulers.computation())
                .flatMapSingle(new Function<AuthEntry, SingleSource<? extends UserEntry>>() {
            @Override
            public SingleSource<? extends UserEntry> apply(AuthEntry authEntry) throws Exception {
                Log.d(LOGTAG, "logIn.flatMap");
                APIService.setCredentials(Credentials.basic(authEntry.getLogin(), authEntry.getPass()));
                UserWrapper userWrapper = new UserWrapperHelper(authEntry.getLogin());
                return userWrapper.createUser()
                        .subscribeOn(Schedulers.io())
                        .toSingle()
                        .onErrorResumeNext(database.userDao()
                                .queryUser(authEntry.getLogin())
                                .subscribeOn(Schedulers.computation()));
            }
        })
                .doOnSuccess(new Consumer<UserEntry>() {
                    @Override
                    public void accept(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "logIn.doOnNext");
                        cache.addUser(userEntry);
                        writeUserInDB(userEntry);
                        loggedUser = userEntry.getLogin();
                    }
                });
    }

    @Override
    public Completable logoutUser() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                database.authDao().deleteAllAuth();
            }
        }).subscribeOn(Schedulers.computation());
    }

    @Override
    public Single<UserEntry> performAuthentication(final String username, final String password) {
        APIService.setCredentials(Credentials.basic(username, password));

        UserWrapper userWrapper = new UserWrapperHelper(username);

        Maybe<UserEntry> networkObservableUserWithWrite = userWrapper.createUser()
                .doOnSuccess(new Consumer<UserEntry>() {
                    @Override
                    public void accept(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "performAuthentication.doOnSuccess");
                        writeAuthInDB(new AuthEntry(username, password));
                        writeUserInDB(userEntry);
                        cache.addUser(userEntry);
                        loggedUser = userEntry.getLogin();
                    }
                });

        return networkObservableUserWithWrite.toSingle();
    }

    @Override
    public Single<UserEntry> queryUser(String loginName) {

        UserWrapper userWrapper = new UserWrapperHelper(loginName);

        Maybe<UserEntry> cacheObservableUser = cache.getUser(loginName)
                .doOnSuccess(new Consumer<UserEntry>() {
                    @Override
                    public void accept(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "queryUser.cacheObservableUser.doOnSuccess");
                        writeUserInDB(userEntry);
                        cache.addUser(userEntry);
                    }
                });

        Maybe<UserEntry> networkObservableUser = userWrapper.createUser()
                .doOnSuccess(new Consumer<UserEntry>() {
                    @Override
                    public void accept(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "queryUser.networkObservableUser.doOnSuccess");
                        writeUserInDB(userEntry);
                        cache.addUser(userEntry);
                    }
                });

        Maybe<UserEntry> dbObservableUser = database.userDao()
                .queryUser(loginName)
                .subscribeOn(Schedulers.computation())
                .doOnSuccess(new Consumer<UserEntry>() {
                    @Override
                    public void accept(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "queryUser.dbObservableUser.doOnSuccess");
                        cache.addUser(userEntry);
                    }
                })
                .toMaybe();

        Single<UserEntry> resultObservable =
                Maybe.concat(cacheObservableUser, networkObservableUser.onErrorResumeNext(dbObservableUser))
                        .firstElement()
                        .toSingle();

        return resultObservable;
    }

    @Override
    public Single<UserEntry> loadMoreOwnedRepos(String loginName) {

        return cache.getUser(loginName).flatMapSingle(new Function<UserEntry, SingleSource<? extends UserEntry>>() {
            @Override
            public SingleSource<? extends UserEntry> apply(UserEntry userEntry) throws Exception {
                UserWrapper userWrapper = new UserWrapperHelper(userEntry);
                return userWrapper.loadMoreOwnedRepos().subscribeOn(Schedulers.io());
            }
        });
    }

    @Override
    public Single<UserEntry> loadMoreStarredRepos(String loginName) {
        return cache.getUser(loginName).flatMapSingle(new Function<UserEntry, SingleSource<? extends UserEntry>>() {
            @Override
            public SingleSource<? extends UserEntry> apply(UserEntry userEntry) throws Exception {
                UserWrapper userWrapper = new UserWrapperHelper(userEntry);
                return userWrapper.loadMoreStarredRepos().subscribeOn(Schedulers.io());
            }
        });
    }

    @Override
    public Single<List<UserEntry>> queryUsers(SearchModel searchModel) {
        lastSearch = searchModel;
        userSearchHelper = new UserSearchPagingHelper();
        Map<String, String> searchMap = new HashMap<>();
        String searchUrl = "";
        switch (searchModel.getSearchType()) {
            case CONTRIBUTORS:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = cache.getRepoContributorsUrl(searchModel.getSearchCriteria());
                break;
            case USER:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = Constants.URL_GIT_API_USER_SEARCH;
                searchMap.put("q", searchModel.getSearchCriteria());
                break;
            case FOLLOWING:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = cache.getUserFollowingUrl(searchModel.getSearchCriteria());
                searchUrl = searchUrl.replace("{/other_user}", "");
                break;
            case FOLLOWERS:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = cache.getUserFollowersUrl(searchModel.getSearchCriteria());
                break;
            default:
                Log.d(LOGTAG, "queryUsers case 'default'");
        }

        Single<List<UserEntry>> networkSearchObs =
                userSearchHelper.search(searchUrl, searchMap);
        if (searchModel.getSearchType() == SearchType.USER) {
            Log.d(LOGTAG, "searchModel.getSearchType() == SearchType.USER");
            return networkSearchObs
                    .subscribeOn(Schedulers.io())
                    .onErrorResumeNext(database.userDao().queryUsers(
                            searchModel.getSearchCriteria()).subscribeOn(Schedulers.computation()));
        }
        return networkSearchObs.subscribeOn(Schedulers.io());
    }

    @Override
    public Single<RepoEntry> queryRepo(final String repoName) {
        Single<RepoEntry> queryRepoObs = apiService.queryRepo(repoName)
                .flatMap(new Function<RepoEntry, SingleSource<? extends RepoEntry>>() {
                    @Override
                    public SingleSource<? extends RepoEntry> apply(final RepoEntry repoEntry) throws Exception {
                        return apiService.getImage(repoEntry.getOwner().getAvatar_url())
                                .flatMap(new Function<ResponseBody, SingleSource<? extends RepoEntry>>() {
                                    @Override
                                    public SingleSource<? extends RepoEntry> apply(ResponseBody responseBody) throws Exception {
                                        if (responseBody != null) {
                                            repoEntry.setRepoOwnerImage(responseBody.bytes());
                                        }
                                        return Single.just(repoEntry);
                                    }
                                });
                    }
                });
        Single<Integer> queryRepoContrObs = RepoBuilder.getRepoContributorsCount(apiService, repoName);
        Single<Integer> queryRepoCommitsObs = RepoBuilder.getRepoCommitsCount(apiService, repoName);
        Single<Integer> queryRepoBranchesObs = RepoBuilder.getRepoBranchesCount(apiService, repoName);
        Single<Integer> queryRepoReleasesObs = RepoBuilder.getRepoReleasesCount(apiService, repoName);

        Maybe<RepoEntry> combinedNetworkObs = Single.zip(queryRepoObs, queryRepoContrObs, queryRepoCommitsObs,
                queryRepoBranchesObs, queryRepoReleasesObs, new Function5<RepoEntry, Integer, Integer, Integer, Integer, RepoEntry>() {
                    @Override
                    public RepoEntry apply(RepoEntry repoEntry, Integer integer, Integer integer2, Integer integer3, Integer integer4) throws Exception {
                        repoEntry.setCommits_count(integer2);
                        repoEntry.setContributors_count(integer);
                        repoEntry.setBranches_count(integer3);
                        repoEntry.setReleases_count(integer4);
                        return repoEntry;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSuccess(new Consumer<RepoEntry>() {
                    @Override
                    public void accept(RepoEntry repoEntry) throws Exception {
                        Log.d(LOGTAG, "queryRepo.combinedNetworkObs.doOnSuccess");
                        cache.addRepo(repoEntry);
                        writeRepoInDB(repoEntry);
                    }
                })
                .toMaybe();

        Maybe<RepoEntry> cacheObs = cache.getRepo(repoName);
        Maybe<RepoEntry> dbObs = database.repoDao().queryRepo(repoName)
                .subscribeOn(Schedulers.computation())
                .doOnSuccess(new Consumer<RepoEntry>() {
                    @Override
                    public void accept(RepoEntry repoEntry) throws Exception {
                        Log.d(LOGTAG, "queryRepo.dbObs.doOnSuccess");
                        cache.addRepo(repoEntry);
                    }
                });

        Single<RepoEntry> finalObs = Maybe.concat(cacheObs, combinedNetworkObs.onErrorResumeNext(dbObs))
                .firstElement()
                .toSingle();

        return finalObs
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(LOGTAG, "queryRepo.finalObs.doOnError");
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public Single<List<UserEntry>> loadMoreSearchResults(SearchModel searchModel) {

        if (compareSearchModels(searchModel)) {
            Single<List<UserEntry>> result = loadMoreResults().subscribeOn(Schedulers.io());
            return result;
        }

        userSearchHelper = new UserSearchPagingHelper();
        Map<String, String> searchMap = new HashMap<>();
        String searchUrl = "";
        switch (searchModel.getSearchType()) {
            case CONTRIBUTORS:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = cache.getRepoContributorsUrl(searchModel.getSearchCriteria());
                break;
            case USER:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = Constants.URL_GIT_API_USER_SEARCH;
                searchMap.put("q", searchModel.getSearchCriteria());
                break;
            case FOLLOWING:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = cache.getUserFollowingUrl(searchModel.getSearchCriteria());
                searchUrl = searchUrl.replace("{/other_user}", "");
                break;
            case FOLLOWERS:
                Log.d(LOGTAG, "queryUsers case " + searchModel.getSearchType());
                searchUrl = cache.getUserFollowersUrl(searchModel.getSearchCriteria());
                break;
            default:
                Log.d(LOGTAG, "queryUsers case 'default'");
        }

        String page = String.valueOf(searchModel.getCurrentResultsCount() / Constants.SEARCH_QUERIES_MAX_PER_PAGE + 1);
        searchMap.put("page", page);

        Single<List<UserEntry>> networkSearchObs =
                userSearchHelper.search(searchUrl, searchMap)
                        .subscribeOn(Schedulers.io());
        return networkSearchObs;
    }

    @Override
    public Completable starRepo(final String repoFullName) {
        Completable writeIfSuccessful = cache.getUser(loggedUser)
                .flatMap(new Function<UserEntry, MaybeSource<?>>() {
                    @Override
                    public MaybeSource<?> apply(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "unststarRepoarRepo.writeIfSuccessful");
                        userEntry.getStarredRepos().add(repoFullName);
                        writeUserInDB(userEntry);
                        return Maybe.just(userEntry);
                    }
                }).ignoreElement();



        return apiService.starRepo(repoFullName)
                .andThen(writeIfSuccessful)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable unstarRepo(final String repoFullName) {

        Completable writeIfSuccessful = cache.getUser(loggedUser)
                .flatMap(new Function<UserEntry, MaybeSource<?>>() {
                    @Override
                    public MaybeSource<?> apply(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "unstarRepo.writeIfSuccessful");
                        userEntry.getStarredRepos().remove(repoFullName);
                        writeUserInDB(userEntry);
                        return Maybe.just(userEntry);
                    }
                }).ignoreElement();

        return apiService.unstarRepo(repoFullName)
                .andThen(writeIfSuccessful)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable isStarredByLoggedUser(final String repoFullName) {
        final Completable checkIfStarredLocalSource = cache.getUser(loggedUser)
                .flatMap(new Function<UserEntry, MaybeSource<?>>() {
                    @Override
                    public MaybeSource<?> apply(UserEntry userEntry) throws Exception {
                        Log.d(LOGTAG, "isStarredByLoggedUser.checkIfStarredLocalSource");
                        if (userEntry.getStarredRepos().contains(repoFullName)) {
                            return Maybe.just(userEntry);
                        }
                        return Maybe.error(new NoSuchElementException());
                    }
                })
                .ignoreElement();

        return apiService.checkIfRepoIsStarred(repoFullName)
                .onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Throwable throwable) throws Exception {
                        Log.d(LOGTAG, "isStarredByLoggedUser.checkIfRepoIsStarred.onErrorResumeNext");
                        return checkIfStarredLocalSource;
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    private void writeUserInDB(final UserEntry userEntry) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(LOGTAG, "writeUserInDB.run");
                database.userDao().insertUser(userEntry);
            }
        });
    }

    private void writeRepoInDB(final RepoEntry repoEntry) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(LOGTAG, "writeRepoInDB.run");
                database.repoDao().insertRepo(repoEntry);
            }
        });
    }

    private void writeAuthInDB(final AuthEntry authEntry) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(LOGTAG, "writeAuthInDB.run");
                database.authDao().insertAuth(authEntry);
            }
        });
    }

    private boolean compareSearchModels(SearchModel newModel) {
        boolean isTypeEqual = lastSearch.getSearchType() == newModel.getSearchType();
        boolean isCriteriaEqual = lastSearch.getSearchCriteria().equals(newModel.getSearchCriteria());
        boolean resultsEqual = lastSearch.getCurrentResultsCount() == newModel.getCurrentResultsCount();
        return  (isTypeEqual && isCriteriaEqual && resultsEqual);
    }

    private Single<List<UserEntry>> loadMoreResults() {
        if (userSearchHelper.hasMorePages()) {
            return userSearchHelper.getNextPage();
        }
        else {
            return Single.error(new NoSuchElementException("No more pages."));
        }
    }

}
