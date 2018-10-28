package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Anton.
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE login = :user")
    Single<UserEntry> queryUser(String user);

    @Query("SELECT * FROM users WHERE login LIKE '%' || :user || '%'")
    Single<List<UserEntry>> queryUsers(String user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntry userEntry);

}
