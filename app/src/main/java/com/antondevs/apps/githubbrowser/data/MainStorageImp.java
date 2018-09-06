package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.CompletableSubject;
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

    private Map<String, RepoEntry> currentUserRepos;

    private MainStorageImp() {
        apiService = APIService.getService();
        currentUserRepos = new HashMap<>();
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
            basicCredentials = Credentials.basic(entry.getLogin(), entry.getPass(), UTF_8);
            listener.onUserAuthenticated();
        }
    }

    @Override
    public void performAuthentication(final String username, final String password, final AuthenticationListener listener) {
        basicCredentials = Credentials.basic(username, password, UTF_8);

        CompletableObserver observer = new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "observer.onSubscribe() " + d.isDisposed());

            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "observer.onComplete()");
                databaseHelper.writeAuthnetication(new AuthEntry(username, password));
                databaseHelper.writeUser(currentUser);
                listener.onUserAuthenticated();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "observer.onError()");
                listener.onAuthenticationFailed();

            }
        };

        createUserFromRemoteSource(basicCredentials, username).subscribe(observer);

    }

    @Override
    public void queryUser(final UserListener listener, final String loginName) {

        if (currentUser != null && currentUser.getLogin().equals(loginName)) {
            listener.onUserLoaded(currentUser);
            return;
        }

        CompletableObserver observer = new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(LOGTAG, "observer.onSubscribe() " + d.isDisposed());

            }

            @Override
            public void onComplete() {
                Log.d(LOGTAG, "observer.onComplete()");
                databaseHelper.writeUser(currentUser);
                listener.onUserLoaded(currentUser);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOGTAG, "observer.onError()");
                listener.onLoadFailed();

            }
        };

        createUserFromRemoteSource(basicCredentials, loginName).subscribe(observer);

    }

    @Override
    public void queryUsers(SearchListener listener, String loginName) {

    }

    @Override
    public void queryRepo(final RepoListener listener, String repoFullName) {

        listener.onRepoLoaded(currentUserRepos.get(repoFullName));

    }

    @Override
    public void queryFollowers(SearchListener listener, String loginName) {

    }

    @Override
    public void queryFollowing(SearchListener listener, String loginName) {

    }

    @Override
    public void queryContributors(SearchListener listener, String repoName) {

    }

    private void notifyUserListener(UserListener listener) {
        if (listener instanceof AuthenticationListener) {
            AuthenticationListener authenticationListener = (AuthenticationListener) listener;
            authenticationListener.onUserAuthenticated();
        }
        else {
            listener.onUserLoaded(currentUser);
        }
    }

    private int getPagesCountFromLinkHeader(String linkHeader) {

        int startIndexForSearch = linkHeader.indexOf("next");

        String searchCriteria = "page=";

        int start = linkHeader.indexOf(searchCriteria, startIndexForSearch);
        int end = linkHeader.indexOf('&', startIndexForSearch);
        String substring = linkHeader.substring(start + searchCriteria.length(), end);
        Log.d(LOGTAG, "getPagesCountFromLinkHeader() substring = " + substring);
        return Integer.valueOf(substring);
    }

    private Completable createUserFromRemoteSource(String authCredentials, String username) {

        Completable userEntryCall = apiService.queryUser(authCredentials, username)
                .doOnSuccess(new Consumer<UserEntry>() {
                    @Override
                    public void accept(UserEntry userEntry) throws Exception {
                        currentUser = userEntry;
                    }
                }).ignoreElement();

        Completable userOwnedCall = apiService.queryUserOwnedRepos(authCredentials, username)
                .doOnSuccess(new Consumer<List<RepoEntry>>() {
                    @Override
                    public void accept(List<RepoEntry> repoEntries) throws Exception {
                        List<String> ownedRepos = new ArrayList<>();
                        for (RepoEntry entry : repoEntries) {
                            ownedRepos.add(entry.getFull_name());
                            currentUserRepos.put(entry.getFull_name(), entry);
                        }
                        currentUser.setOwnedRepos(ownedRepos);
                    }
                }).ignoreElement();

        Completable userStarredCall = apiService.queryUserStarredRepos(authCredentials, username)
                .doOnSuccess(new Consumer<List<RepoEntry>>() {
                    @Override
                    public void accept(List<RepoEntry> repoEntries) throws Exception {
                        List<String> starredRepos = new ArrayList<>();
                        for (RepoEntry entry : repoEntries) {
                            starredRepos.add(entry.getFull_name());
                            currentUserRepos.put(entry.getFull_name(), entry);
                        }
                        currentUser.setStarredRepos(starredRepos);
                    }
                }).ignoreElement();

        Completable completeable = userEntryCall.subscribeOn(Schedulers.io())
                .concatWith(userOwnedCall.subscribeOn(Schedulers.io()))
                .concatWith(userStarredCall.subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return completeable;

    }
}
