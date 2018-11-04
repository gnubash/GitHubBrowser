package com.antondevs.apps.githubbrowser.ui;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Anton.
 */
public abstract class AbsGitHubPresenter implements BasePresenter {

    private static final String LOGTAG = AbsGitHubPresenter.class.getSimpleName();

    public MainStorage storage;
    CommonViewBehavior viewBehavior;

    public AbsGitHubPresenter(CommonViewBehavior view, MainStorage mainStorage) {
        viewBehavior = view;
        storage = mainStorage;
    }

    @Override
    public void logout() {
        storage.logoutUser().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "logout.onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOGTAG, "logout.onComplete");
                        viewBehavior.goToLogin();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "logout.onError");
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void home() {
        Log.d(LOGTAG, "home");
        viewBehavior.goToUser(storage.getLoggedUser());
    }
}
