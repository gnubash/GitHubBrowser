package com.antondevs.apps.githubbrowser.ui.login;

import com.antondevs.apps.githubbrowser.ui.BaseView;

/**
 * Created by Anton.
 */
public interface LoginContract {

    interface LoginView extends BaseView {

        void requestAuthentication();

        void onUserAuthenticated();

        void showAuthErrorMsg();

        void showNetworkErrorMsg();

    }

    interface LoginPresenter {

        void authenticateUser(String username, String password);

        void loginWithStoredCredentials();

        void destroyPresenter();

        String getLogedUsername();
    }
}
