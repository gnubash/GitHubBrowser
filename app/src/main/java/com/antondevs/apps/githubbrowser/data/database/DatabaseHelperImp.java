package com.antondevs.apps.githubbrowser.data.database;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

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
    public void writeAuthnetication(final AuthEntry authEntry) {
        databaseExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(LOGTAG, "writeAuthnetication = " + authEntry.toString());
                database.authDao().insertAuth(authEntry);
            }
        });
    }

    @Override
    public int getStoredAuth() {

        Future<Integer> storedCredentials = databaseExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Log.d(LOGTAG, "Future.getStoredAuth().call()");
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

        Future<AuthEntry> authEntryFuture = databaseExecutor.submit(new Callable<AuthEntry>() {
            @Override
            public AuthEntry call() throws Exception {
                Log.d(LOGTAG, "Future.getAuthentication().call()");
                return database.authDao().getAuth();
            }
        });

        AuthEntry entry = null;
        try {
            entry = authEntryFuture.get();
            Log.d(LOGTAG, "entry = " + entry);
        }
        catch (InterruptedException | ExecutionException e) {
            Log.d(LOGTAG, "Exception occurred while Future.get() in getAuthentication()");
        }
        Log.d(LOGTAG, "entry = " + entry);
        return entry;
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
    public UserEntry getUser(final String username) {

        Future<UserEntry> userEntryFuture = databaseExecutor.submit(new Callable<UserEntry>() {
            @Override
            public UserEntry call() throws Exception {
                Log.d(LOGTAG, "Future.getUser().call()");
                return database.userDao().queryUser(username);
            }
        });
        UserEntry user = null;
        try {
            user = userEntryFuture.get();
        }
        catch (InterruptedException | ExecutionException e) {
            Log.d(LOGTAG, "Exception occurred while Future.get() in getUser()");
        }
        return user;
    }

    @Override
    public RepoEntry getRepo(String repoName) {
        return null;
    }

    @Override
    public void writeUser(final UserEntry user) {
        databaseExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(LOGTAG, "writeUser = " + user.toString());
                database.userDao().insertUser(user);
            }
        });
    }

    @Override
    public void writeRepo(RepoEntry repo) {

    }

    @Override
    public List<UserEntry> searchByName(String username) {
        return null;
    }
}
