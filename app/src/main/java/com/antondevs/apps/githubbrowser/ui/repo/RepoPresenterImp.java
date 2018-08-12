package com.antondevs.apps.githubbrowser.ui.repo;

import android.util.Log;

/**
 * Created by Anton.
 */
public class RepoPresenterImp implements RepoContract.Presenter {

    private static final String LOGTAG = RepoPresenterImp.class.getSimpleName();

    private String repoName;

    private RepoContract.View view;

    public RepoPresenterImp(String repoName, RepoContract.View view) {
        Log.d(LOGTAG, "RepoPresenterImp()");
        this.repoName = repoName;
        this.view = view;
    }

    @Override
    public void loadPresenter() {

    }

    @Override
    public void starRepo() {

    }

    @Override
    public String getRepoName() {
        return repoName;
    }
}
