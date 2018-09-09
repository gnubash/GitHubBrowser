package com.antondevs.apps.githubbrowser.data.database.model;

/**
 * Created by Anton.
 */
public class CommitEntry {

    private String sha;
    private String node_id;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    @Override
    public String toString() {
        return sha + "\n" + node_id;
    }
}
