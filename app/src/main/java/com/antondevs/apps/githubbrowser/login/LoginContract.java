package com.antondevs.apps.githubbrowser.login;

/**
 * Created by Anton on 6/30/18.
 */
public interface LoginContract {

    interface LoginPresenter {

        void checkAuthentication();

        void authenticateUser(String username, String password);
    }

    interface LoginView {

        void onUserAuthenticated(String loginName);

        void onRequestUserAuthentication();

        void onAuthenticationFailed(String errorMessage);
    }
}
