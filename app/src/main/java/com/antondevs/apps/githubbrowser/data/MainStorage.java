package com.antondevs.apps.githubbrowser.data;

import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;

import java.util.List;

/**
 * Created by Anton on 6/30/18.
 */
public interface MainStorage {

    String getLoggedUser();

    void setDatabaseHelper(DatabaseHelper databaseHelper);

    void checkCredentials(AuthenticationListener listener);

    void performAuthentication(String username, String password, AuthenticationListener listener);

    void queryUser(UserListener listener, String loginName);

    void queryUsers(SearchListener listener, String loginName);

    void queryRepo(RepoListener listener, String repoName);

    void queryFollowers(SearchListener listener, String loginName);

    void queryFollowing(SearchListener listener, String loginName);

    void queryContributors(SearchListener listener, String repoName);

    interface AuthenticationListener {

        void onUserAuthenticated();

        void onAuthenticationRequered();

        void onAuthenticationFailed();

        void onNetworkConnectionFailure();

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
