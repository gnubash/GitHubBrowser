package com.antondevs.apps.githubbrowser.ui.search;

import com.antondevs.apps.githubbrowser.data.database.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;
import com.antondevs.apps.githubbrowser.utilities.DatabaseUtils;

import java.util.ArrayList;

/**
 * Created by Anton.
 */
public class SearchPresenterImp implements SearchContract.Presenter {

    private static final String LOGTAG = SearchPresenterImp.class.getSimpleName();

    private SearchContract.View view;

    public SearchPresenterImp(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void searchUser(String userName) {
        view.showNoResultsView();
    }

    @Override
    public void searchContributors(String repoName) {
        view.setSearchResult(convertUserListToStringList());
    }

    @Override
    public void searchFollowers(String userName) {
        view.setSearchResult(convertUserListToStringList());
    }

    @Override
    public void searchFollowing(String username) {
        view.setSearchResult(convertUserListToStringList());
    }

    private ArrayList<String> convertUserListToStringList() {
        ArrayList<UserEntry> userList = new ArrayList<>();
        //fill the list
        for (int i = 0; i < 20; i++) {
            userList.add(DatabaseUtils.generateUser());
        }
        ArrayList<String> userNames = new ArrayList<>();
        for (UserEntry user : userList) {
            userNames.add(user.getLogin());
        }

        return userNames;

    }
}