package com.antondevs.apps.githubbrowser.ui.repo;

import android.net.Uri;

import com.antondevs.apps.githubbrowser.ui.BasePresenter;
import com.antondevs.apps.githubbrowser.ui.BaseView;
import com.antondevs.apps.githubbrowser.ui.CommonViewBehavior;

/**
 * Created by Anton.
 */
public interface RepoContract {

    interface View extends BaseView, CommonViewBehavior {

        void setOwnerImageUri(Uri uri);

        void setOwnerName(String ownerName);

        void setRepoName(String repoName);

        void setCommits(String commitsNumber);

        void setBranches(String branchesNumber);

        void setReleases(String releasesNumber);

        void setContributors(String contributorsNumber);

        void setStar(String starNumber);

        void setFork(String forkNumber);

        void showNoData();

        void setStarredStatus(boolean isStarred);

    }

    interface Presenter extends BasePresenter {

        void loadPresenter();

        void starRepo();

        String getRepoName();
    }
}
