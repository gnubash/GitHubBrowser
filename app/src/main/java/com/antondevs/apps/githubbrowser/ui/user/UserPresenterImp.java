package com.antondevs.apps.githubbrowser.ui.user;

import com.antondevs.apps.githubbrowser.data.database.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;
import com.antondevs.apps.githubbrowser.utilities.DatabaseUtils;

import java.util.ArrayList;

/**
 * Created by Anton on 7/8/18.
 */
public class UserPresenterImp implements UserContract.UserPresenter {

    private String mUserLoginName;

    private final UserContract.UserView mView;

    public UserPresenterImp(String userLoginName, UserContract.UserView view) {
        mUserLoginName = userLoginName;
        mView = view;
    }

    @Override
    public void loadPresenter() {
        UserEntry user = DatabaseUtils.generateUser();
        mView.setFollowers(String.valueOf(user.getFollowing()));
        mView.setFollowing(String.valueOf(user.getFollowing()));
        mView.setUserName(user.getLogin());
        mView.setReposList(convertRepoListToStringList());
    }

    @Override
    public void getFollowers(String username) {

    }

    @Override
    public void getFollowing(String username) {

    }

    @Override
    public void getOwnedRepos() {

    }

    @Override
    public void getStarredRepos() {

    }

    private ArrayList<String> convertRepoListToStringList() {
        UserEntry testUser = DatabaseUtils.generateUser();
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
}
