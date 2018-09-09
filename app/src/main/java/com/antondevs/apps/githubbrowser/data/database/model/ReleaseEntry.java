package com.antondevs.apps.githubbrowser.data.database.model;

/**
 * Created by Anton.
 */
public class ReleaseEntry {

    private String ref;
    private String node_id;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return node_id;
    }

    public void setName(String name) {
        this.node_id = name;
    }

    @Override
    public String toString() {
        return ref + node_id;
    }
}
