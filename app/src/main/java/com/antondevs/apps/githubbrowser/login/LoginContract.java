package com.antondevs.apps.githubbrowser.login;

/**
 * Created by Anton on 6/30/18.
 */
public interface LoginContract {

    interface LoginPresenter {

        void authenticateUser(String username, String password);
        void loginWithStoredCredentials();
    }

    interface LoginView {

        void requestAuthentication();

        void onUserAuthenticated();

        void onAuthenticationFailed(String errorMessage);
    }
}
