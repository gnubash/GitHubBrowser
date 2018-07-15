package com.antondevs.apps.githubbrowser.ui.repo;

/**
 * Created by Anton on 7/3/18.
 */
public interface RepoContract {

    interface View {

        void setOwner(String ownerName);

        void setCommits(String commitsNumber);

        void setBranches(String branchesNumber);

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
