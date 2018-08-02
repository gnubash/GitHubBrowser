package com.antondevs.apps.githubbrowser.data.database;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Anton.
 */
public class DatabaseHelperImp implements DatabaseHelper {

    private static final String LOGTAG = DatabaseHelperImp.class.getSimpleName();

    private static ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private GitHubBrowserDatabase database;

    public DatabaseHelperImp(GitHubBrowserDatabase database) {
        this.database = database;

    }

    @Override
    public void writeAuthnetication(AuthEntry authEntry) {

    }

    @Override
    public int getStoredAuth() {

        Future<Integer> storedCredentials = databaseExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return database.authDao().getNumberOfStoredCredentials();
            }
        });

        int tempNumber = 0;
        try {
            tempNumber = storedCredentials.get();
        }
        catch (InterruptedException | ExecutionException e) {
            Log.d(LOGTAG, "Exception occurred while Future.get() in getStoredAuth()");
        }
        Log.d(LOGTAG, "tempNumber = " + tempNumber);
        return tempNumber;
    }

    @Override
    public AuthEntry getAuthentication() {
        return null;
    }

    @Override
    public void clearStoredAuth() {
        databaseExecutor.submit(new Runnable() {
            @Override
            public void run() {
                database.authDao().getAuth();
            }
        });
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
