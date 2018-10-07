package com.antondevs.apps.githubbrowser.ui.user;

import com.antondevs.apps.githubbrowser.ui.BaseView;

import java.util.List;

/**
 * Created by Anton.
 */
public interface UserContract {

    interface UserView extends BaseView{

        void setUserName(String name);

        void setFollowers(String followersNumber);

        void setFollowing(String followingNumber);

        void setOwnedReposList(List<String> repoEntryList);

        void setStarredReposList(List<String> repoEntryList);

    }


    interface UserPresenter {

        void loadPresenter();

        void scrollOwnedToBottom();

        void scrollStarredToBottom();

        String getUserLoginName();

    }
}
