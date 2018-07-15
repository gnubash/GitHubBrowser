package com.antondevs.apps.githubbrowser.ui.user;

import com.antondevs.apps.githubbrowser.data.database.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;
import com.antondevs.apps.githubbrowser.utilities.DatabaseUtils;

import java.util.ArrayList;

/**
 * Created by Anton on 7/8/18.
 */
public class UserPresenterImp implements UserContract.UserPresenter {

    private String userLoginName;

    private final UserContract.UserView view;

    public UserPresenterImp(String userLoginName, UserContract.UserView view) {
        this.userLoginName = userLoginName;
        this.view = view;
    }

    @Override
    public void loadPresenter() {
        UserEntry user = DatabaseUtils.generateUser();
        view.setFollowers(String.valueOf(user.getFollowers()));
        view.setFollowing(String.valueOf(user.getFollowing()));
        view.setUserName(user.getLogin());
        view.setReposList(convertRepoListToStringList());
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
}
