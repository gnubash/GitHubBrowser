package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Anton.
 */
@Dao
public interface RepoDao {

    @Query("SELECT * FROM repos ORDER BY repos_id")
    List<RepoEntry> queryAllRepos();

    @Query("SELECT * FROM repos WHERE full_name LIKE :repoName")
    RepoEntry queryRepo(String repoName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepo(RepoEntry repo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRepo(RepoEntry repo);

    @Delete
    void removeRepo(RepoEntry repo);
}
