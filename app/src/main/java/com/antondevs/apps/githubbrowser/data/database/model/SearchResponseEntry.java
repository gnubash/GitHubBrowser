package com.antondevs.apps.githubbrowser.data.database.model;

import java.util.List;

/**
 * Created by Anton.
 */
public class SearchResponseEntry {

    List<UserEntry> items;

    public List<UserEntry> getItems() {
        return items;
    }

    public void setItems(List<UserEntry> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
