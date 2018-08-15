package com.antondevs.apps.githubbrowser.ui.login;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

/**
 * Created by Anton on 6/30/18.
 */
public class LoginPresenterImp implements LoginContract.LoginPresenter,
        MainStorage.AuthenticationListener {

    private LoginContract.LoginView view;
    private MainStorage storage;

    public LoginPresenterImp(LoginContract.LoginView view, MainStorage storage) {
        this.view = view;
        this.storage = storage;
    }

    @Override
    public void loginWithStoredCredentials() {
        storage.checkCredentials(this);
    }

    @Override
    public void authenticateUser(String username, String password) {
        view.showLoading();
        storage.performAuthentication(username, password, this);
    }

    @Override
    public void destroyPresenter() {
        view = null;
        storage = null;
    }

    @Override
    public void onUserLoaded(UserEntry userEntry) {

    }

    @Override
    public void onLoadFailed() {

    }

    @Override
    public String getLogedUsername() {
        return storage.getLoggedUser();
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
    public void onAuthenticationFailed() {
        view.showAuthErrorMsg();
    }

    @Override
    public void onNetworkConnectionFailure() {
        view.showNetworkErrorMsg();
    }
}
