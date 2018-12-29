package com.antondevs.apps.githubbrowser.ui.search;

import android.net.Uri;

import com.antondevs.apps.githubbrowser.ui.BasePresenter;
import com.antondevs.apps.githubbrowser.ui.BaseView;
import com.antondevs.apps.githubbrowser.ui.CommonViewBehavior;

/**
 * Created by Anton.
 */
public interface SearchContract {

    interface View extends BaseView, CommonViewBehavior {

        void resultsLoaded();

        void showNoResultsView();

        void showNoMoreSearchResults();

        void showLoadingMoreResults();

    }

    interface Presenter extends BasePresenter, PresenterSearchResults {

        void searchUser(String userName);

        void searchContributors(String repoName);

        void searchFollowers(String userName);

        void searchFollowing(String userName);

        void userScrollToBottom();

    }

    interface ViewSearchResultItem {

        void setLoginName(String loginName);

        void setImageUri(Uri uri);
    }

    interface PresenterSearchResults {

        void bindViewToPosition(int position, ViewSearchResultItem viewSearchResultItem);

        int getItemsCount();
    }
}
