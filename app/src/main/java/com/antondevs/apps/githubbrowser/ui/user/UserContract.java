package com.antondevs.apps.githubbrowser.ui.user;

/**
 * Created by Anton on 7/1/18.
 */
public interface UserContract {

    interface UserView {

        void setUserName(String name);

        void setFollowers(String followersNumber);

        void setFollowing(String followingNumber);
    }


    interface UserPresenter {

        void init();

        void getFollowers(String username);

        void getFollowing(String username);
    }
}
