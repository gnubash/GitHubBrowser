package com.antondevs.apps.githubbrowser.ui.search;

import com.antondevs.apps.githubbrowser.data.database.UserEntry;

import java.util.List;

/**
 * Created by Anton on 7/3/18.
 */
public interface SearchContract {

    interface View {

        void setSearchResult(List<UserEntry> userList);

        void showNoResultsView();

    }

    interface Presenter {

        void searchUser(String username);
        
    }
}
