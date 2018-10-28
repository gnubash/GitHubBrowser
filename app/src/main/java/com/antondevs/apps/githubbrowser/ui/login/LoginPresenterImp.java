package com.antondevs.apps.githubbrowser.ui.login;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.io.IOException;
import java.util.NoSuchElementException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by Anton.
 */
public class LoginPresenterImp implements LoginContract.LoginPresenter {

    private static final String LOGTAG = LoginPresenterImp.class.getSimpleName();

    private LoginContract.LoginView view;
    private MainStorage storage;
    private String loginName;

    public LoginPresenterImp(LoginContract.LoginView view, MainStorage storage) {
        this.view = view;
        this.storage = storage;
    }

    @Override
    public void authenticateUser(String username, String password) {
        storage.performAuthentication(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntry>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "authenticateUser.onSubscribe");
                    }

                    @Override
                    public void onSuccess(UserEntry userEntry) {
                        Log.d(LOGTAG, "authenticateUser.onSuccess");
                        loginName = userEntry.getLogin();
                        view.onUserAuthenticated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "authenticateUser.onError");
                        if (e instanceof IOException) {
                            Log.d(LOGTAG, "authenticateUser.onError.IOException");
                            view.showNetworkErrorMsg();
                        }
                        else {
                            Log.d(LOGTAG, "authenticateUser.onError.other");
                            e.printStackTrace();
                            view.showAuthErrorMsg();
                        }
                    }
                });
    }

    @Override
    public void loginWithStoredCredentials() {
        storage.logIn().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserEntry>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "loginWithStoredCredentials.onSubscribe");
                        view.showLoading();
                    }

                    @Override
                    public void onSuccess(UserEntry userEntry) {
                        Log.d(LOGTAG, "loginWithStoredCredentials.onSuccess");
                        loginName = userEntry.getLogin();
                        view.onUserAuthenticated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "loginWithStoredCredentials.onError");
                        if (e instanceof IOException) {
                            Log.d(LOGTAG, "loginWithStoredCredentials.onError.IOException");
                        }
                        else if (e instanceof NoSuchElementException) {
                            view.requestAuthentication();
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void destroyPresenter() {

    }

    @Override
    public String getLogedUsername() {
        Log.d(LOGTAG, "getLogedUsername = " + loginName);
        return loginName;
    }
}
