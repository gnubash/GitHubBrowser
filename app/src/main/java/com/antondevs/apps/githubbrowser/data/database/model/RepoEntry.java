package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Anton.
 */
@Entity(tableName = "repos", indices = {@Index(value = {"id"}, unique = true)})
public class RepoEntry {

    @PrimaryKey(autoGenerate = true)
    private int repos_id;

    @ColumnInfo(name = "id")
    private long id;

    private String full_name;

    private String contributors_url;

    private int commits_count;
    private int branches_count;
    private int releases_count;
    private int contributors_count;

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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getContributors_url() {
        return contributors_url;
    }

    public void setContributors_url(String contributors_url) {
        this.contributors_url = contributors_url;
    }

    public int getCommits_count() {
        return commits_count;
    }

    public void setCommits_count(int commits_count) {
        this.commits_count = commits_count;
    }

    public int getBranches_count() {
        return branches_count;
    }

    public void setBranches_count(int branches_count) {
        this.branches_count = branches_count;
    }

    public int getReleases_count() {
        return releases_count;
    }

    public void setReleases_count(int releases_count) {
        this.releases_count = releases_count;
    }

    public int getContributors_count() {
        return contributors_count;
    }

    public void setContributors_count(int contributors_count) {
        this.contributors_count = contributors_count;
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
        StringBuilder asString = new StringBuilder();
        String separator = " ";
        
        asString.append(id).append(separator)
                .append(full_name).append(separator)
                .append(contributors_url).append(separator)
                .append(commits_count).append(separator)
                .append(branches_count).append(separator)
                .append(releases_count).append(separator)
                .append(contributors_count).append(separator)
                .append(forks).append(separator)
                .append(watchers);

        return asString.toString();
    }
}
