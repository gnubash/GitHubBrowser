package com.antondevs.apps.githubbrowser.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import okhttp3.Credentials;

/**
 * Created by Anton.
 */
@Entity(tableName = "auth")
public class AuthEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private Credentials authKey;

    public AuthEntry() {

    }

    public Credentials getAuthKey() {
        return authKey;
    }

    public void setAuthKey(Credentials authKey) {
        this.authKey = authKey;
    }

    @Override
    public String toString() {
        return authKey.toString();
    }
}
