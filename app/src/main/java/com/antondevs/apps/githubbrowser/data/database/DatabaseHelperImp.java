package com.antondevs.apps.githubbrowser.data.database;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by Anton.
 */
public class DatabaseHelperImp implements DatabaseHelper {

    private static final String LOGTAG = DatabaseHelperImp.class.getSimpleName();

    private Executor databaseExecutor;

    public DatabaseHelperImp() {

    }

    @Override
    public void writeAuthnetication(AuthEntry authEntry) {

    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public AuthEntry getAuthentication() {
        return null;
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
