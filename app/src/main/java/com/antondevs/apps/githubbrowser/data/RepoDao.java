package com.antondevs.apps.githubbrowser.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antondevs.apps.githubbrowser.data.entries.RepoEntry;

/**
 * Created by Anton on 6/28/18.
 */
@Dao
interface RepoDao {

    @Query("SELECT * FROM repos WHERE name LIKE :repoName")
    RepoEntry queryRepo(String repoName);

    @Insert
    void insertRepo(RepoEntry repo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRepo(RepoEntry repo);

    @Delete
    void removeRepo(RepoEntry repo);
}
