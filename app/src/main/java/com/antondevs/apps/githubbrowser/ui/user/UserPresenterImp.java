package com.antondevs.apps.githubbrowser.ui.user;

import com.antondevs.apps.githubbrowser.data.MainInteractor;
import com.antondevs.apps.githubbrowser.data.database.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;
import com.antondevs.apps.githubbrowser.utilities.DatabaseUtils;

import java.util.ArrayList;

/**
 * Created by Anton on 7/8/18.
 */
public class UserPresenterImp implements UserContract.UserPresenter, MainInteractor.UserListener {

    private String userLoginName;

    private final UserContract.UserView view;

    MainInteractor storage;

    public UserPresenterImp(String userLoginName, UserContract.UserView view, MainInteractor storage) {
        this.userLoginName = userLoginName;
        this.view = view;
        this.storage = storage;
    }

    @Override
    public void loadPresenter() {

        storage.queryUser(this, userLoginName);

    }

    @Override
    public void getOwnedRepos() {
        view.setReposList(convertRepoListToStringList());
    }

    @Override
    public void getStarredRepos() {
        view.setReposList(convertRepoListToStringList());
    }

    private ArrayList<String> convertRepoListToStringList() {
        ArrayList<RepoEntry> reposList = new ArrayList<>();
        //fill the list
        for (int i = 0; i < 20; i++) {
            reposList.add(DatabaseUtils.generateRepor());
        }
        ArrayList<String> reposNames = new ArrayList<>();
        for (RepoEntry repo : reposList) {
            reposNames.add(repo.getName());
        }

        return reposNames;

    }

    @Override
    public String getUserLoginName() {
        return userLoginName;
    }

    @Override
    public void onUserLoaded(UserEntry userEntry) {

        view.setFollowers(String.valueOf(userEntry.getFollowers()));
        view.setFollowing(String.valueOf(userEntry.getFollowing()));
        view.setUserName(userEntry.getLogin());
        view.setReposList(convertRepoListToStringList());
    }

    @Override
    public void onLoadFailed() {

    }
}
