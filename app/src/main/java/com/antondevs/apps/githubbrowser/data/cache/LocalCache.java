package com.antondevs.apps.githubbrowser.data.cache;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Anton.
 */
public interface LocalCache {

    Maybe<UserEntry> getUser(String loginName);

    Maybe<RepoEntry> getRepo(String repoFullName);

    void addUser(UserEntry userEntry);

    void addRepo(RepoEntry repoEntry);

    String getRepoContributorsUrl(String repoName);

    String getUserFollowersUrl(String loginName);

    String getUserFollowingUrl(String loginName);

}
