package com.antondevs.apps.githubbrowser.data.database.model;

/**
 * Created by Anton.
 */
public class ReleaseEntry {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + name;
    }
}
