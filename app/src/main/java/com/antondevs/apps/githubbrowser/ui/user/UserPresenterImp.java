package com.antondevs.apps.githubbrowser.ui.user;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;


/**
 * Created by Anton.
 */
public class UserPresenterImp implements UserContract.UserPresenter, MainStorage.UserListener {

    private static final String LOGTAG = UserPresenterImp.class.getSimpleName();

    private String userLoginName;

    private UserEntry currentUserEntry;

    private final UserContract.UserView view;

    MainStorage storage;

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
        storage.queryUser(this, userLoginName);

    }

    @Override
    public void scrollOwnedToBottom() {
        if (!isLoadingOwned) {
            isLoadingOwned = true;
            storage.loadMoreOwnedRepos(this, currentUserEntry.getLogin());
        }
    }

    @Override
    public void scrollStarredToBottom() {
        if (!isLoadingOwned) {
            isLoadingStarred = true;
            storage.loadMoreStarredRepos(this, currentUserEntry.getLogin());
        }
    }

    @Override
    public String getUserLoginName() {
        return currentUserEntry.getLogin();
    }

    @Override
    public void onUserLoaded(UserEntry userEntry) {

        Log.d(LOGTAG, "onUserLoaded" + userEntry.toString());

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

    @Override
    public void onLoadFailed() {
        
    }
}
