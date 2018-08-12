package com.antondevs.apps.githubbrowser.ui.user;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.utilities.DatabaseUtils;

import java.util.ArrayList;

/**
 * Created by Anton on 7/8/18.
 */
public class UserPresenterImp implements UserContract.UserPresenter, MainStorage.UserListener {

    private String userLoginName;

    private final UserContract.UserView view;

    MainStorage storage;

    public UserPresenterImp(String userLoginName, UserContract.UserView view, MainStorage storage) {
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

    }

    @Override
    public void getStarredRepos() {

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
