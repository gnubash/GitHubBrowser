package com.antondevs.apps.githubbrowser.ui.repo;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;

/**
 * Created by Anton.
 */
public class RepoPresenterImp implements RepoContract.Presenter {

    private static final String LOGTAG = RepoPresenterImp.class.getSimpleName();

    private String repoName;

    private RepoContract.View view;

    private MainStorage storage;

    public RepoPresenterImp(String repoName, RepoContract.View view, MainStorage storage) {
        Log.d(LOGTAG, "RepoPresenterImp()");
        this.repoName = repoName;
        this.view = view;
        this.storage = storage;
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
