package com.antondevs.apps.githubbrowser.ui.search;

import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.ui.BasePresenter;
import com.antondevs.apps.githubbrowser.ui.BaseView;
import com.antondevs.apps.githubbrowser.ui.CommonViewBehavior;

import java.util.List;

/**
 * Created by Anton.
 */
public interface SearchContract {

    interface View extends BaseView, CommonViewBehavior {

        void setSearchResult(List<UserEntry> userList);

        void showNoResultsView();

        void showNoMoreSearchResults();

        void showLoadingMoreResults();

    }

    interface Presenter extends BasePresenter {

        void searchUser(String userName);

        void searchContributors(String repoName);

        void searchFollowers(String userName);

        void searchFollowing(String userName);

        void userScrollToBottom();

    }
}
