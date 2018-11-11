package com.antondevs.apps.githubbrowser.data;

import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.ui.search.SearchModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Anton.
 */
public interface MainStorage {

    void setDatabaseHelper(GitHubBrowserDatabase appDatabase);

    String getLoggedUser();

    Single<UserEntry> logIn();

    Completable logoutUser();

    Single<UserEntry> performAuthentication(String username, String password);

    Single<UserEntry> queryUser(String loginName);

    Single<UserEntry> loadMoreOwnedRepos(String loginName);

    Single<UserEntry> loadMoreStarredRepos(String loginName);

    Single<List<UserEntry>> queryUsers(SearchModel searchModel);

    Single<RepoEntry> queryRepo(String repoName);

    Single<List<UserEntry>> loadMoreSearchResults(SearchModel searchModel);

    Completable starRepo(String repoFullName);

    Completable unstarRepo(String repoFullName);

    Completable isStarredByLoggedUser(String repoFullName);

}
