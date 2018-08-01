package com.antondevs.apps.githubbrowser.ui.login;

import com.antondevs.apps.githubbrowser.data.MainStorage;

/**
 * Created by Anton on 6/30/18.
 */
public class LoginPresenterImp implements LoginContract.LoginPresenter,
        MainStorage.AuthenticationListener {

    private LoginContract.LoginView view;
    private MainStorage interactor;

    public LoginPresenterImp(LoginContract.LoginView view, MainStorage interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void loginWithStoredCredentials() {
        interactor.checkCredentials(this);
    }

    @Override
    public void authenticateUser(String username, String password) {
        interactor.performAuthentication(username, password, this);
    }

    @Override
    public void onUserAuthenticated() {
        view.onUserAuthenticated();
    }

    @Override
    public void onAuthenticationRequered() {
       view.requestAuthentication();
    }

    @Override
    public void destroyPresenter() {
        view = null;
        interactor = null;
    }

    @Override
    public void onAuthenticationFailed() {
        view.displayErrorMessage();
    }
}
