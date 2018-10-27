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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function5;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;

/**
 * Created by Anton.
 */
public class MainStorageImp implements MainStorage {

    private static final String LOGTAG = MainStorageImp.class.getSimpleName();
    private static final MainStorage instance = new MainStorageImp();

    private GitHubBrowserDatabase database;
    private ResponsePaging<List<UserEntry>> userSearchHelper;

    private RemoteAPIService apiService;
    private LocalCache cache;

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
                    }
                });
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
                    }
                });

        return networkObservableUserWithWrite.toSingle();
    }

    @Override
    public Single<UserEntry> queryUser(String loginName) {

        UserWrapper userWrapper = new UserWrapperHelper(loginName);

        Maybe<UserEntry> cacheObservableUser = cache.getUser(loginName);
        Maybe<UserEntry> networkObservableUser = userWrapper.createUser();

        Maybe<UserEntry> dbObservableUser = database.userDao()
                .queryUser(loginName)
                .subscribeOn(Schedulers.computation())
                .toMaybe();

        Single<UserEntry> resultObservable =
                Maybe.concat(cacheObservableUser, networkObservableUser.onErrorResumeNext(dbObservableUser))
                        .firstElement()
                        .doOnSuccess(new Consumer<UserEntry>() {
                            @Override
                            public void accept(UserEntry userEntry) throws Exception {
                                Log.d(LOGTAG, "queryUser.doOnSuccess");
                                writeUserInDB(userEntry);
                                cache.addUser(userEntry);
                            }
                        })
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
        ResponsePaging<List<UserEntry>> userSearchPaging = new UserSearchPagingHelper();
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
                userSearchPaging.search(searchUrl, searchMap);
        if (searchModel.getSearchType() == SearchType.USER) {
            Log.d(LOGTAG, "searchModel.getSearchType() == SearchType.USER");
            return networkSearchObs
                    .subscribeOn(Schedulers.io())
                    .onErrorResumeNext(database.userDao().queryUsers(searchModel.getSearchCriteria()));
        }
        return networkSearchObs.subscribeOn(Schedulers.io());
    }

    @Override
    public Single<RepoEntry> queryRepo(String repoName) {
        Single<RepoEntry> queryRepoObs = apiService.queryRepo(repoName);
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
                .toMaybe();

        Maybe<RepoEntry> cacheObs = cache.getRepo(repoName);
        Maybe<RepoEntry> dbObs = database.repoDao().queryRepo(repoName);

        Single<RepoEntry> finalObs = Maybe.concat(cacheObs, combinedNetworkObs.onErrorResumeNext(dbObs))
                .firstElement()
                .toSingle();


        return finalObs
                .doOnSuccess(new Consumer<RepoEntry>() {
                    @Override
                    public void accept(RepoEntry repoEntry) throws Exception {
                        Log.d(LOGTAG, "queryRepo.finalObs.doOnSuccess.accept");
                        cache.addRepo(repoEntry);
                        writeRepoInDB(repoEntry);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(LOGTAG, "queryRepo.finalObs.doOnError.accept");
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public Single<List<UserEntry>> loadMoreSearchResults(SearchModel searchModel) {

        ResponsePaging<List<UserEntry>> userSearchPaging = new UserSearchPagingHelper();
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
                userSearchPaging.search(searchUrl, searchMap)
                        .subscribeOn(Schedulers.io());
        return networkSearchObs;
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

    private void queryUsersByRepo() {

    }

}
