package com.antondevs.apps.githubbrowser.data.remote;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anton.
 */
public class UserWrapperHelper implements UserWrapper {

    private static final String LOGTAG = UserWrapperHelper.class.getSimpleName();
    private static final int DEFAULT_PER_PAGE_RESPONSE = 30;

    private String ownedUrl;
    private String starredUrl;

    private UserEntry currentUser;

    private String loginName;

    private boolean initNewOwnedSearch;
    private boolean initNewStarredSearch;

    private RemoteAPIService service;
    private ResponsePaging<List<RepoEntry>> ownedReposPaging;
    private ResponsePaging<List<RepoEntry>> starredReposPaging;

    public UserWrapperHelper(String loginName) {
        Log.d(LOGTAG, "UserWrapperHelper");
        this.loginName = loginName;
        service = APIService.getService();
        ownedReposPaging = new RepoResponsePaging();
        starredReposPaging = new RepoResponsePaging();
    }

    public UserWrapperHelper(UserEntry user) {
        Log.d(LOGTAG, "UserWrapperHelper " + user.getLogin());
        service = APIService.getService();
        currentUser = user;
        ownedUrl = currentUser.getRepos_url();
        starredUrl = currentUser.getStarred_url().replace("{/owner}{/repo}", "");
        ownedReposPaging = new RepoResponsePaging();
        starredReposPaging = new RepoResponsePaging();
        initNewOwnedSearch = true;
        initNewStarredSearch = true;
    }

    @Override
    public Maybe<UserEntry> createUser() {

        if (currentUser != null) {
            return Maybe.just(currentUser);
        }

        Log.d(LOGTAG, "createUser " + loginName);

        return createUserFromRemoteSource(loginName).andThen(Maybe.defer(new Callable<MaybeSource<? extends UserEntry>>() {
            @Override
            public MaybeSource<? extends UserEntry> call() throws Exception {
                return Maybe.just(currentUser);
            }
        }));
    }

    @Override
    public Single<UserEntry> loadMoreOwnedRepos() {

        if (initNewOwnedSearch) {
            if (currentUser.getOwnedRepos().size() % DEFAULT_PER_PAGE_RESPONSE != 0) {
                Log.d(LOGTAG, "loadMoreOwnedRepos.initNewOwnedSearch.if");
                return Single.just(currentUser);
            }
            int nextPage = currentUser.getOwnedRepos().size() / DEFAULT_PER_PAGE_RESPONSE + 1;
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("page", String.valueOf(nextPage));

            Log.d(LOGTAG, "loadMoreOwnedRepos.initNewStarredSearch " + queryMap.toString());

            return ownedReposPaging.search(ownedUrl, queryMap)
                    .flatMap(new Function<List<RepoEntry>, SingleSource<? extends UserEntry>>() {
                        @Override
                        public SingleSource<? extends UserEntry> apply(List<RepoEntry> repoEntries) throws Exception {
                            List<String> currentRepos = currentUser.getOwnedRepos();
                            List<String> newRepos = new ArrayList<>();
                            for (RepoEntry repo : repoEntries) {
                                newRepos.add(repo.getFull_name());
                            }
                            currentRepos.addAll(newRepos);
                            currentUser.setOwnedRepos(currentRepos);
                            return Single.just(currentUser);
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            initNewOwnedSearch = false;
                        }
                    });
        }

        if (ownedReposPaging.hasMorePages()) {
            Log.d(LOGTAG, "loadMoreStarredRepos.ownedReposPaging.hasMorePages");
            return ownedReposPaging.getNextPage()
                    .flatMap(new Function<List<RepoEntry>, SingleSource<? extends UserEntry>>() {
                        @Override
                        public SingleSource<? extends UserEntry> apply(List<RepoEntry> repoEntries) throws Exception {
                            List<String> currentRepos = currentUser.getOwnedRepos();
                            List<String> newRepos = new ArrayList<>();
                            for (RepoEntry repo : repoEntries) {
                                newRepos.add(repo.getFull_name());
                            }
                            currentRepos.addAll(newRepos);
                            currentUser.setOwnedRepos(currentRepos);
                            Log.d(LOGTAG, "loadMoreStarredRepos.ownedReposPaging.hasMorePages " + currentUser.toString());
                            return Single.just(currentUser);
                        }
                    });
        }
        else {
            return Single.just(currentUser);
        }
    }

    @Override
    public Single<UserEntry> loadMoreStarredRepos() {

        if (initNewStarredSearch) {
            if (currentUser.getStarredRepos().size() % DEFAULT_PER_PAGE_RESPONSE != 0) {
                Log.d(LOGTAG, "loadMoreStarredRepos.initNewOwnedSearch.if");
                return Single.just(currentUser);
            }
            int nextPage = currentUser.getStarredRepos().size() / DEFAULT_PER_PAGE_RESPONSE + 1;
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("page", String.valueOf(nextPage));

            Log.d(LOGTAG, "loadMoreStarredRepos.initNewStarredSearch " + queryMap.toString());

            return starredReposPaging.search(starredUrl, queryMap)
                    .flatMap(new Function<List<RepoEntry>, SingleSource<? extends UserEntry>>() {
                        @Override
                        public SingleSource<? extends UserEntry> apply(List<RepoEntry> repoEntries) throws Exception {
                            List<String> currentRepos = currentUser.getStarredRepos();
                            List<String> newRepos = new ArrayList<>();
                            for (RepoEntry repo : repoEntries) {
                                newRepos.add(repo.getFull_name());
                            }
                            currentRepos.addAll(newRepos);
                            currentUser.setStarredRepos(currentRepos);
                            return Single.just(currentUser);
                        }
                    }).doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            initNewStarredSearch = false;
                        }
                    });
        }

        if (starredReposPaging.hasMorePages()) {
            Log.d(LOGTAG, "loadMoreStarredRepos.starredReposPaging.hasMorePages");
            return starredReposPaging.getNextPage()
                    .flatMap(new Function<List<RepoEntry>, SingleSource<? extends UserEntry>>() {
                        @Override
                        public SingleSource<? extends UserEntry> apply(List<RepoEntry> repoEntries) throws Exception {
                            List<String> currentRepos = currentUser.getStarredRepos();
                            List<String> newRepos = new ArrayList<>();
                            for (RepoEntry repo : repoEntries) {
                                newRepos.add(repo.getFull_name());
                            }
                            currentRepos.addAll(newRepos);
                            currentUser.setStarredRepos(currentRepos);
                            Log.d(LOGTAG, "loadMoreStarredRepos.starredReposPaging.hasMorePages" + currentUser.toString());
                            return Single.just(currentUser);
                        }
                    });
        }
        else {
            return Single.just(currentUser);
        }
    }

    private Completable createUserFromRemoteSource(String username) {

        Completable userEntryCall = service.queryUser(username)
                .doOnSuccess(new Consumer<UserEntry>() {
                    @Override
                    public void accept(UserEntry userEntry) throws Exception {
                        currentUser = userEntry;
                        ownedUrl = userEntry.getRepos_url();
                        Log.d(LOGTAG, "starred_url before replacement " + currentUser.getStarred_url());
                        String replacedStarredUrl = currentUser.getStarred_url().replace("{/owner}{/repo}", "");
                        Log.d(LOGTAG, "starred_url after replacement " + replacedStarredUrl);
                        starredUrl = replacedStarredUrl;
                    }
                }).ignoreElement();


        Completable completeable = userEntryCall
                .andThen(Completable.defer(new Callable<CompletableSource>() {
                    @Override
                    public CompletableSource call() throws Exception {
                        return ownedReposPaging.search(ownedUrl,
                                new HashMap<String, String>()).flatMapCompletable(new Function<List<RepoEntry>, CompletableSource>() {
                            @Override
                            public CompletableSource apply(List<RepoEntry> repoEntries) throws Exception {
                                List<String> ownedRepos = new ArrayList<>();
                                for (RepoEntry entry : repoEntries) {
                                    ownedRepos.add(entry.getFull_name());
                                }
                                currentUser.setOwnedRepos(ownedRepos);
                                Log.d(LOGTAG, "createUserFromRemoteSource ownedRepos = " + ownedRepos.size());
                                return Completable.complete();
                            }
                        });
                    }
                }))
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(LOGTAG, "createUserFromRemoteSource.Completable.andThen.defer.doOnError");
                        throwable.printStackTrace();
                    }
                })
                .andThen(Completable.defer(new Callable<CompletableSource>() {
                    @Override
                    public CompletableSource call() throws Exception {
                        Log.d(LOGTAG, "createUserFromRemoteSource.starredReposPaging " + starredUrl);
                        return starredReposPaging.search(starredUrl,
                                new HashMap<String, String>()).flatMapCompletable(new Function<List<RepoEntry>, CompletableSource>() {
                            @Override
                            public CompletableSource apply(List<RepoEntry> repoEntries) throws Exception {
                                List<String> starredRepos = new ArrayList<>();
                                for (RepoEntry entry : repoEntries) {
                                    starredRepos.add(entry.getFull_name());
                                }
                                currentUser.setStarredRepos(starredRepos);
                                Log.d(LOGTAG, "createUserFromRemoteSource starredRepos = " + starredRepos.size());
                                return Completable.complete();
                            }
                        });
                    }
                }))
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(LOGTAG, "createUserFromRemoteSource.Completable.andThen.andThen.defer.doOnError");
                        throwable.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        return completeable;

    }

}
