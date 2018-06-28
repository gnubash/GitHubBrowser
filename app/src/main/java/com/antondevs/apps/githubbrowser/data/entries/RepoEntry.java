package com.antondevs.apps.githubbrowser.data.entries;

/**
 * Created by Anton on 6/24/18.
 */

public class RepoEntry {

    private int mId;

    private String login;
    private String name;

    private int forks;
    private int watchers;

    public RepoEntry() {

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
}
