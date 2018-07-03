package com.antondevs.apps.githubbrowser.ui.user;

import com.antondevs.apps.githubbrowser.data.database.UserEntry;

import java.util.List;

/**
 * Created by Anton on 7/1/18.
 */
public interface UserContract {

    interface UserView {

        void setUserName(String name);

        void setFollowers(String followersNumber);

        void setFollowing(String followingNumber);

        void setFollowList(List<UserEntry> userEntryList);
    }


    interface UserPresenter {

        void loadUser(String username);

        void getFollowers(String username);

        void getFollowing(String username);

    }
}