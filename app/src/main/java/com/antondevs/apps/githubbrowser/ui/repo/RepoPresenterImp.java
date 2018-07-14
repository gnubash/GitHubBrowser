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
        // TODO Set some fake data for testing purposes
    }

    @Override
    public void starRepo() {
        // TODO Should attempt to star the repo. If Manager says "failure" then maybe notify the View
    }
}
