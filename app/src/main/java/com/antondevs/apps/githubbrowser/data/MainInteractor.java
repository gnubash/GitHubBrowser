package com.antondevs.apps.githubbrowser.data;

/**
 * Created by Anton on 6/30/18.
 */
public interface MainInteractor {

    void checkCredentials();
    void performAuthentication(String username, String password);

    interface Listener {

        void onUserAuthenticated();

        void onAuthenticationRequered();

    }

}
