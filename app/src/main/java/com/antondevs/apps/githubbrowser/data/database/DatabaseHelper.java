package com.antondevs.apps.githubbrowser.data.database;

import java.util.List;

/**
 * Created by Anton.
 */
public interface DatabaseHelper {

    List<UserEntry> getContributors(String repoName);

    List<RepoEntry> getUserStarredRepos(String userName);

    List<RepoEntry> getUserOwnedRepos(String username);

    List<UserEntry> getFollowers(String username);

    List<UserEntry> getFollowing(String username);

    UserEntry getUser(String username);

    RepoEntry getRepo(String repoName);

    void writeUser(UserEntry user);

    void writeRepo(RepoEntry repo);

    UserEntry searchByName(String username);
}
