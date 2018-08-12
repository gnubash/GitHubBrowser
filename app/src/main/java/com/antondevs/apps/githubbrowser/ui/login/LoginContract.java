package com.antondevs.apps.githubbrowser.ui.login;

/**
 * Created by Anton on 6/30/18.
 */
public interface LoginContract {

    interface LoginView {

        void requestAuthentication();

        void onUserAuthenticated();

        void displayAuthErrorMsg();

        void displayNetworkErrorMsg();
    }

    interface LoginPresenter {

        void authenticateUser(String username, String password);

        void loginWithStoredCredentials();

        void destroyPresenter();

        String getLogedUsername();
    }
}
