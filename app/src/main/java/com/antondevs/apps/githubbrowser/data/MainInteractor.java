package com.antondevs.apps.githubbrowser.data;

/**
 * Created by Anton on 6/30/18.
 */
public interface MainInteractor {

    void checkCredentials();

    interface Listener {

        void onUserAuthenticated();

        void onAuthenticationRequered();

    }

}
