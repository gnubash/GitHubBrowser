package com.antondevs.apps.githubbrowser.ui.user;

import android.graphics.Bitmap;

import com.antondevs.apps.githubbrowser.ui.BasePresenter;
import com.antondevs.apps.githubbrowser.ui.BaseView;
import com.antondevs.apps.githubbrowser.ui.CommonViewBehavior;

import java.util.List;

/**
 * Created by Anton.
 */
public interface UserContract {

    interface UserView extends BaseView, CommonViewBehavior {

        void setUserImage(byte [] imageAsByteArray);

        void setUserName(String name);

        void setFollowers(String followersNumber);

        void setFollowing(String followingNumber);

        void setOwnedReposList(List<String> repoEntryList);

        void setStarredReposList(List<String> repoEntryList);

        void showNoData();

    }


    interface UserPresenter extends BasePresenter {

        void loadPresenter();

        void scrollOwnedToBottom();

        void scrollStarredToBottom();

        String getUserLoginName();

    }
}
