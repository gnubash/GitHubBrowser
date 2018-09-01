package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Anton on 6/24/18.
 */
@Entity(tableName = "repos")
public class RepoEntry {

    @PrimaryKey(autoGenerate = true)
    private int repos_id;
    private long id;

    private String full_name;

    private int forks;
    private int watchers;

    public RepoEntry() {

    }

    public int getRepos_id() {
        return repos_id;
    }

    public void setRepos_id(int repos_id) {
        this.repos_id = repos_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return full_name;
    }

    public void setName(String full_name) {
        this.full_name = full_name;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    @Override
    public String toString() {
        return "id:" + id + " full_name:" + full_name + " forks:" + forks + " watchers:" + watchers;
    }
}
