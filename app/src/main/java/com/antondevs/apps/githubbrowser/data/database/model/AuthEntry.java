package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import okhttp3.Credentials;

/**
 * Created by Anton.
 */
@Entity(tableName = "auth")
public class AuthEntry {

    @PrimaryKey(autoGenerate = true)
    private int auth_id;

    private String login;
    private String pass;

    public AuthEntry(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public int getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(int auth_id) {
        this.auth_id = auth_id;
    }

    public int getId() {
        return auth_id;
    }

    public void setId(int id) {
        this.auth_id = id;
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
        return auth_id + login + pass;
    }
}
