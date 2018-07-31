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

    private String login;
    private String pass;

    public AuthEntry(String login, String pass) {
        this.login = login;
        this.pass = pass;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return login + pass;
    }
}
