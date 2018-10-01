package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;
import com.antondevs.apps.githubbrowser.data.remote.ResponsePaging;
import com.antondevs.apps.githubbrowser.data.remote.UserWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Observer;
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
        ownedReposPaging = new RepoResponsePaging();
        starredReposPaging = new RepoResponsePaging();
        initNewOwnedSearch = true;
        initNewStarredSearch = true;
    }

    @Override
    public Observable<UserEntry> createUser() {

        if (currentUser != null) {
            return Observable.just(currentUser);
        }

        Log.d(LOGTAG, "createUser " + loginName);

        return createUserFromRemoteSource(loginName).andThen(new Observable<UserEntry>() {
            @Override
            protected void subscribeActual(Observer<? super UserEntry> observer) {
                Log.d(LOGTAG, "createUser.subscribeActual");
                observer.onNext(currentUser);
                observer.onComplete();
            }
        });
    }

    @Override
    public Observable<List<RepoEntry>> loadMoreOwnedRepos() {

        if (initNewOwnedSearch) {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("page", "2");
            queryMap.put("per_page", String.valueOf(currentUser.getOwnedRepos().size()));
            return ownedReposPaging.search(currentUser.getRepos_url(), queryMap).doFinally(new Action() {
                @Override
                public void run() throws Exception {
                    initNewOwnedSearch = false;
                }
            });
        }

        if (ownedReposPaging.hasMorePages()) {
            return ownedReposPaging.getNextPage().doOnNext(new Consumer<List<RepoEntry>>() {
                @Override
                public void accept(List<RepoEntry> repoEntries) throws Exception {
                    List<String> currentRepos = currentUser.getOwnedRepos();
                    List<String> newRepos = new ArrayList<>();
                    for (RepoEntry repo : repoEntries) {
                        newRepos.add(repo.getFull_name());
                    }
                    currentRepos.addAll(newRepos);
                    currentUser.setOwnedRepos(newRepos);
                }
            });
        }
        else {
            return Observable.empty();
        }
    }

    @Override
    public Observable<List<RepoEntry>> loadMoreStarredRepos() {

        if (initNewStarredSearch) {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put("page", "2");
            queryMap.put("per_page", String.valueOf(currentUser.getOwnedRepos().size()));
            return starredReposPaging.search(currentUser.getStarred_url(), queryMap).doFinally(new Action() {
                @Override
                public void run() throws Exception {
                    initNewStarredSearch = false;
                }
            });
        }

        if (starredReposPaging.hasMorePages()) {
            return starredReposPaging.getNextPage().doOnNext(new Consumer<List<RepoEntry>>() {
                @Override
                public void accept(List<RepoEntry> repoEntries) throws Exception {
                    List<String> currentRepos = currentUser.getStarredRepos();
                    List<String> newRepos = new ArrayList<>();
                    for (RepoEntry repo : repoEntries) {
                        newRepos.add(repo.getFull_name());
                    }
                    currentRepos.addAll(newRepos);
                    currentUser.setStarredRepos(newRepos);
                }
            });
        }
        else {
            return Observable.empty();
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
                                return Completable.complete();
                            }
                        });
                    }
                }))
                .andThen(Completable.defer(new Callable<CompletableSource>() {
                    @Override
                    public CompletableSource call() throws Exception {
                        return starredReposPaging.search(starredUrl,
                                new HashMap<String, String>()).flatMapCompletable(new Function<List<RepoEntry>, CompletableSource>() {
                            @Override
                            public CompletableSource apply(List<RepoEntry> repoEntries) throws Exception {
                                List<String> starredRepos = new ArrayList<>();
                                for (RepoEntry entry : repoEntries) {
                                    starredRepos.add(entry.getFull_name());
                                }
                                currentUser.setStarredRepos(starredRepos);
                                return Completable.complete();
                            }
                        });
                    }
                })).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        return completeable;

    }
}
