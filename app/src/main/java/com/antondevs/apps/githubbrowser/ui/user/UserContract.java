package com.antondevs.apps.githubbrowser.ui.user;

import java.util.List;

/**
 * Created by Anton on 7/1/18.
 */
public interface UserContract {

    interface UserView {

        void setUserName(String name);

        void setFollowers(String followersNumber);

        void setFollowing(String followingNumber);

        void setReposList(List<String> repoEntryList);
    }


    interface UserPresenter {

        void loadPresenter();

        void getFollowers(String username);

        void getFollowing(String username);

        void getOwnedRepos();

        void getStarredRepos();

    }
}
