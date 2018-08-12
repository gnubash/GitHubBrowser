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
    private int id;

    private String login;
    private String name;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "id:" + id + " login:" + login + " name:" + name + " forks:" + forks + " watchers:" + watchers;
    }
}
