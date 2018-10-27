package com.antondevs.apps.githubbrowser.ui.user;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by Anton.
 */
public class UserPresenterImp implements UserContract.UserPresenter {

    private static final String LOGTAG = UserPresenterImp.class.getSimpleName();

    private String userLoginName;

    private UserEntry currentUserEntry;

    private final UserContract.UserView view;

    private MainStorage storage;

    private boolean isLoadingOwned;
    private boolean isLoadingStarred;

    public UserPresenterImp(String userLoginName, UserContract.UserView view, MainStorage storage) {
        this.userLoginName = userLoginName;
        this.view = view;
        this.storage = storage;
    }

    @Override
    public void loadPresenter() {
        view.showLoading();
        storage.queryUser(userLoginName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntry>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "loadPresenter.queryUser.onSubscribe");
                    }

                    @Override
                    public void onSuccess(UserEntry userEntry) {
                        Log.d(LOGTAG, "loadPresenter.queryUser.onSuccess");
                        configureView(userEntry);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "loadPresenter.queryUser.onError");
                        e.printStackTrace();
                    }
                });

    }

    @Override
    public void scrollOwnedToBottom() {
        if (!isLoadingOwned) {
            isLoadingOwned = true;
            Log.d(LOGTAG, "scrollOwnedToBottom");
            storage.loadMoreOwnedRepos(currentUserEntry.getLogin())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<UserEntry>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(LOGTAG, "scrollOwnedToBottom.onSubscribe");
                        }

                        @Override
                        public void onSuccess(UserEntry userEntry) {
                            Log.d(LOGTAG, "scrollOwnedToBottom.onSuccess");
                            configureView(userEntry);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(LOGTAG, "scrollOwnedToBottom.onError");
                        }
                    });
        }
    }

    @Override
    public void scrollStarredToBottom() {
        if (!isLoadingStarred) {
            isLoadingStarred = true;
            Log.d(LOGTAG, "scrollStarredToBottom");
            storage.loadMoreStarredRepos(currentUserEntry.getLogin())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<UserEntry>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(LOGTAG, "scrollStarredToBottom.onSubscribe");
                        }

                        @Override
                        public void onSuccess(UserEntry userEntry) {
                            Log.d(LOGTAG, "scrollStarredToBottom.onSuccess");
                            configureView(userEntry);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(LOGTAG, "scrollStarredToBottom.onError");
                        }
                    });
        }
    }

    @Override
    public String getUserLoginName() {
        return currentUserEntry.getLogin();
    }

    private void configureView(UserEntry userEntry) {
        Log.d(LOGTAG, "configureView" + userEntry.toString());

        currentUserEntry = userEntry;

        view.setFollowers(String.valueOf(userEntry.getFollowers()));
        view.setFollowing(String.valueOf(userEntry.getFollowing()));
        view.setUserName(userEntry.getLogin());
        view.setOwnedReposList(userEntry.getOwnedRepos());
        view.setStarredReposList(userEntry.getStarredRepos());

        view.showViews();

        isLoadingOwned = false;
        isLoadingStarred = false;
    }
}
