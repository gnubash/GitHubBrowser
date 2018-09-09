package com.antondevs.apps.githubbrowser.ui.repo;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;

/**
 * Created by Anton.
 */
public class RepoPresenterImp implements RepoContract.Presenter, MainStorage.RepoListener {

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
        view.showLoading();
        storage.queryRepo(this, repoName);
    }

    @Override
    public void starRepo() {

    }

    @Override
    public String getRepoName() {
        return repoName;
    }

    @Override
    public void onRepoLoaded(RepoEntry repoEntry) {
        String [] repoOwnerAndName = repoEntry.getFull_name().split("/");
        view.setOwnerName(repoOwnerAndName[0]);
        view.setRepoName(repoOwnerAndName[1]);
        view.setFork(String.valueOf(repoEntry.getForks()));
        view.setStar(String.valueOf(repoEntry.getWatchers()));
        view.setContributors(String.valueOf(repoEntry.getContributors_count()));
        view.setCommits(String.valueOf(repoEntry.getCommits_count()));
        view.setBranches(String.valueOf(repoEntry.getBranches_count()));
        view.setReleases(String.valueOf(repoEntry.getReleases_count()));
        view.showViews();
    }

    @Override
    public void onLoadFailed() {

    }

}
