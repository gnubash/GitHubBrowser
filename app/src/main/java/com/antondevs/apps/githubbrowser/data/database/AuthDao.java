package com.antondevs.apps.githubbrowser.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Anton.
 */
@Dao
public interface AuthDao {

    @Query("SELECT Count(*) FROM auth")
    int getNumberOfStoredCredentials();

    @Insert
    void insertAuth(AuthEntry entry);

    @Query("DELETE FROM auth")
    void deleteAllAuth();
}
