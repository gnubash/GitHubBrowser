package com.antondevs.apps.githubbrowser.ui.user;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;


/**
 * Created by Anton.
 */
public class UserPresenterImp implements UserContract.UserPresenter, MainStorage.UserListener {

    private String userLoginName;

    private UserEntry currentUserEntry;

    private final UserContract.UserView view;

    MainStorage storage;

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
    public void getOwnedRepos() {
        view.setReposList(currentUserEntry.getOwnedRepos());
    }

    @Override
    public void getStarredRepos() {
        view.setReposList(currentUserEntry.getStarredRepos());
    }

    @Override
    public void scrollOwnedToBottom() {

    }

    @Override
    public void scrollStarredToBottom() {

    }

    @Override
    public String getUserLoginName() {
        return currentUserEntry.getLogin();
    }

    @Override
    public void onUserLoaded(UserEntry userEntry) {

        currentUserEntry = userEntry;

        view.setFollowers(String.valueOf(userEntry.getFollowers()));
        view.setFollowing(String.valueOf(userEntry.getFollowing()));
        view.setUserName(userEntry.getLogin());
        view.setReposList(userEntry.getOwnedRepos());

        view.showViews();
    }

    @Override
    public void onLoadFailed() {
        
    }
}
