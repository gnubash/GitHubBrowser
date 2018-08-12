package com.antondevs.apps.githubbrowser.data.database;

import com.antondevs.apps.githubbrowser.data.database.model.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;

/**
 * Created by Anton.
 */
public interface DatabaseHelper {

    void writeAuthnetication(AuthEntry authEntry);

    int getStoredAuth();

    AuthEntry getAuthentication();

    void clearStoredAuth();

    UserEntry getUser(String username);

    RepoEntry getRepo(String repoName);

    void writeUser(UserEntry user);

    void writeRepo(RepoEntry repo);

    List<UserEntry> searchByName(String username);
}
