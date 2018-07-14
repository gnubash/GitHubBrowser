package com.antondevs.apps.githubbrowser.ui.repo;

import com.antondevs.apps.githubbrowser.data.database.UserEntry;

import java.util.List;

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

        void loadPresenter(String name);

        void starRepo();
    }
}
