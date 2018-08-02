package com.antondevs.apps.githubbrowser.data.database;

import java.util.List;

/**
 * Created by Anton.
 */
public interface DatabaseHelper {

    void writeAuthnetication(AuthEntry authEntry);

    int getStoredAuth();

    AuthEntry getAuthentication();

    UserEntry getUser(String username);

    RepoEntry getRepo(String repoName);

    void writeUser(UserEntry user);

    void writeRepo(RepoEntry repo);

    List<UserEntry> searchByName(String username);
}
