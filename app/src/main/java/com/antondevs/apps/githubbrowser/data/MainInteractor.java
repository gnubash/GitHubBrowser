package com.antondevs.apps.githubbrowser.data;

import com.antondevs.apps.githubbrowser.data.database.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;

import java.util.List;

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

    interface SearchListener {

        void onNoResultsFound();

        void onSearchSuccess(List<UserEntry> userList);

    }

    interface RepoListener {

        void onRepoLoaded(RepoEntry repoEntry);

        void onLoadFailed();
    }

    interface UserListener {

        void onUserLoaded(UserEntry userEntry);

        void onLoadFailed();
    }

}
