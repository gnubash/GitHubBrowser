package com.antondevs.apps.githubbrowser.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antondevs.apps.githubbrowser.data.entries.UserEntry;

/**
 * Created by Anton on 6/28/18.
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE login LIKE :user")
    UserEntry queryUser(String user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntry userEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(UserEntry userEntry);

    @Delete
    void deleteUser(UserEntry userEntry);
}
