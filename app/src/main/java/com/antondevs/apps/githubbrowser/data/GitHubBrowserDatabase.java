package com.antondevs.apps.githubbrowser.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.antondevs.apps.githubbrowser.data.entries.RepoEntry;
import com.antondevs.apps.githubbrowser.data.entries.UserEntry;

/**
 * Created by Anton on 6/29/18.
 */
@Database(entities = {UserEntry.class, RepoEntry.class}, version = 1, exportSchema = false)
public abstract class GitHubBrowserDatabase extends RoomDatabase {

    private static final String LOG_TAG = GitHubBrowserDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "github_browser";
    private static final Object LOCK = new Object();

    private volatile static GitHubBrowserDatabase databaseInstance;

    public static GitHubBrowserDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            synchronized (LOCK) {
                if (databaseInstance == null) {
                    Log.d(LOG_TAG, "Creating database");

                    databaseInstance = Room.databaseBuilder(context, GitHubBrowserDatabase.class,
                            DATABASE_NAME)
                            .build();
                }
            }
        }
        Log.d(LOG_TAG, "Returning database instance");
        return databaseInstance;
    }

    public abstract UserDao userDao();

    public abstract RepoDao repoDao();
}
