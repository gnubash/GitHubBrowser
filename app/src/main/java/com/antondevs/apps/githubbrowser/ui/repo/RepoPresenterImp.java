package com.antondevs.apps.githubbrowser.ui.repo;

import android.net.Uri;
import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.ui.AbsGitHubPresenter;

import java.io.IOException;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Anton.
 */
public class RepoPresenterImp extends AbsGitHubPresenter implements RepoContract.Presenter{

    private static final String LOGTAG = RepoPresenterImp.class.getSimpleName();

    private String repoName;

    private RepoContract.View view;

    private boolean isStarred;

    private boolean starringInProgress;

    public RepoPresenterImp(String repoName, RepoContract.View view, MainStorage storage) {
        super(view, storage);
        Log.d(LOGTAG, "RepoPresenterImp()");
        this.repoName = repoName;
        this.view = view;
        this.storage = storage;
    }

    @Override
    public void loadPresenter() {
        view.showLoading();
        storage.queryRepo(repoName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RepoEntry>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "loadPresenter.onSubscribe");
                    }

                    @Override
                    public void onSuccess(RepoEntry repoEntry) {
                        Log.d(LOGTAG, "loadPresenter.onSuccess");
                        configureView(repoEntry);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "loadPresenter.onError");
                        e.printStackTrace();
                        view.showNoData();
                    }
                });
        storage.isStarredByLoggedUser(repoName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "loadPresenter.isStarredByLoggedUser.onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOGTAG, "loadPresenter.isStarredByLoggedUser.onComplete");
                        isStarred = true;
                        view.setStarredStatus(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "loadPresenter.isStarredByLoggedUser.onError");
                        isStarred = false;
                        view.setStarredStatus(false);
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void starRepo() {
        if (!starringInProgress && !isStarred) {
            starringInProgress = true;
            storage.starRepo(repoName).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(LOGTAG, "loadPresenter.starRepo.onSubscribe");
                        }

                        @Override
                        public void onComplete() {
                            Log.d(LOGTAG, "loadPresenter.starRepo.doOnComplete");
                            isStarred = true;
                            view.setStarredStatus(true);
                            starringInProgress = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(LOGTAG, "loadPresenter.starRepo.onError");
                            if (e instanceof IOException) {
                                Log.d(LOGTAG, "loadPresenter.starRepo.onError IOException");
                            }
                            starringInProgress = false;
                            e.printStackTrace();
                        }
                    });
        }
        if (!starringInProgress && isStarred) {
            starringInProgress = true;
            storage.unstarRepo(repoName).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(LOGTAG, "loadPresenter.starRepo.onSubscribe");
                        }

                        @Override
                        public void onComplete() {
                            Log.d(LOGTAG, "loadPresenter.starRepo.doOnComplete");
                            isStarred = false;
                            view.setStarredStatus(false);
                            starringInProgress = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(LOGTAG, "loadPresenter.starRepo.onError");
                            if (e instanceof IOException) {
                                Log.d(LOGTAG, "loadPresenter.starRepo.onError IOException");
                            }
                            starringInProgress = false;
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public String getRepoName() {
        return repoName;
    }

    private void configureView(RepoEntry repoEntry) {
        Log.d(LOGTAG, "configureView " + repoEntry.toString());
        String [] repoOwnerAndName = repoEntry.getFull_name().split("/");

        // Check is mandatory until Owner is added to repo when reading from database
        if (repoEntry.getOwner() != null) {
            view.setOwnerImageUri(Uri.parse(repoEntry.getOwner().getAvatar_url()));
        }

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

}
