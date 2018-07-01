package com.antondevs.apps.githubbrowser.data;

/**
 * Created by Anton on 6/30/18.
 */
public interface MainInteractor {

    void checkCredentials(AuthenticationListener listener);
    void performAuthentication(String username, String password, AuthenticationListener listener);

    interface AuthenticationListener {

        void onUserAuthenticated();

        void onAuthenticationRequered();

        void onAuthenticationFailed();

    }

}
