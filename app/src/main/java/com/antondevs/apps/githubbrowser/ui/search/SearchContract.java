package com.antondevs.apps.githubbrowser.ui.search;

import com.antondevs.apps.githubbrowser.ui.BaseView;

import java.util.List;

/**
 * Created by Anton on 7/3/18.
 */
public interface SearchContract {

    interface View extends BaseView{

        void setSearchResult(List<String> userList);

        void showNoResultsView();

    }

    interface Presenter {

        void searchUser(String userName);

        void searchContributors(String repoName);

        void searchFollowers(String userName);

        void searchFollowing(String username);

    }
}
