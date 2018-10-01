package com.antondevs.apps.githubbrowser.data;

import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;

/**
 * Created by Anton.
 */
public interface MainStorage {

    String getLoggedUser();

    void setDatabaseHelper(DatabaseHelper databaseHelper);

    void checkCredentials(AuthenticationListener listener);

    void performAuthentication(String username, String password, AuthenticationListener listener);

    void queryUser(UserListener listener, String loginName);

    void loadMoreOwnedRepos(UserListener listener, String loginName);

    void loadMoreStarredRepos(UserListener listener, String loginName);

    void queryUsers(SearchListener listener, String loginName);

    void queryRepo(RepoListener listener, String repoName);

    void queryFollowers(SearchListener listener, String loginName);

    void queryFollowing(SearchListener listener, String loginName);

    void queryContributors(SearchListener listener, String repoName);

    void loadMoreSearchResults(SearchListener listener);

    interface AuthenticationListener {

        void onUserAuthenticated();

        void onAuthenticationRequered();

        void onAuthenticationFailed();

        void onNetworkConnectionFailure();

    }

    interface SearchListener {

        void onNoResultsFound();

        void onSearchSuccess(List<UserEntry> userList);

        void onNoMoreResults();

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
