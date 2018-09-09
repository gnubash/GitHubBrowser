package com.antondevs.apps.githubbrowser.data.database.model;

/**
 * Created by Anton.
 */
public class BranchEntry {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
