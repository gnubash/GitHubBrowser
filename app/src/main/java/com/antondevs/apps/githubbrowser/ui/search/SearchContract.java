package com.antondevs.apps.githubbrowser.ui.search;

import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.ui.BaseView;

import java.util.List;

/**
 * Created by Anton on 7/3/18.
 */
public interface SearchContract {

    interface View extends BaseView{

        void setSearchResult(List<UserEntry> userList);

        void showNoResultsView();

        void showNoMoreSearchResults();

    }

    interface Presenter {

        void searchUser(String userName);

        void searchContributors(String repoName);

        void searchFollowers(String userName);

        void searchFollowing(String userName);

        void userScrollToBottom();

    }
}
