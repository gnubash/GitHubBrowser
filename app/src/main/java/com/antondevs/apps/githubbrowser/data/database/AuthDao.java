package com.antondevs.apps.githubbrowser.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Anton.
 */
@Dao
public interface AuthDao {

    @Query("SELECT * FROM auth WHERE id = 0")
    AuthEntry getAuth();

    @Query("SELECT Count(*) FROM auth")
    int getNumberOfStoredCredentials();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAuth(AuthEntry entry);

    @Query("DELETE FROM auth")
    void deleteAllAuth();
}
