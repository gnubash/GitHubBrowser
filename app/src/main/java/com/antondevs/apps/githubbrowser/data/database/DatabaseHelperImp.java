package com.antondevs.apps.githubbrowser.data.database;

import com.antondevs.apps.githubbrowser.data.database.model.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;

/**
 * Created by Anton.
 */
public class DatabaseHelperImp implements DatabaseHelper{

    public DatabaseHelperImp(GitHubBrowserDatabase database) {

    }

    @Override
    public void writeAuthnetication(AuthEntry authEntry) {

    }

    @Override
    public int getStoredAuth() {
        return 0;
    }

    @Override
    public AuthEntry getAuthentication() {
        return null;
    }

    @Override
    public void clearStoredAuth() {

    }

    @Override
    public UserEntry getUser(String username) {
        return null;
    }

    @Override
    public RepoEntry getRepo(String repoName) {
        return null;
    }

    @Override
    public void writeUser(UserEntry user) {

    }

    @Override
    public void writeRepo(RepoEntry repo) {

    }

    @Override
    public List<UserEntry> searchByName(String username) {
        return null;
    }
}
