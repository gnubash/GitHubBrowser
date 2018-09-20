package com.antondevs.apps.githubbrowser.ui.repo;

import com.antondevs.apps.githubbrowser.ui.BaseView;

/**
 * Created by Anton.
 */
public interface RepoContract {

    interface View extends BaseView{

        void setOwnerName(String ownerName);

        void setRepoName(String repoName);

        void setCommits(String commitsNumber);

        void setBranches(String branchesNumber);

        void setReleases(String releasesNumber);

        void setContributors(String contributorsNumber);

        void setStar(String starNumber);

        void setFork(String forkNumber);

    }

    interface Presenter {

        void loadPresenter();

        void starRepo();

        String getRepoName();
    }
}
