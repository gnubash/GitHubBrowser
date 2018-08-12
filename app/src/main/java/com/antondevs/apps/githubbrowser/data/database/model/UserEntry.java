package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Anton on 6/24/18.
 */
@Entity(tableName = "users")
public class UserEntry {

    @PrimaryKey(autoGenerate = true)
    private int users_id;

    private int id;

    private String login;

    private int followers;
    private int following;

    public UserEntry() {

    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
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

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "id:" + id + " login:" + login + " followers:" + followers + " following:" + following;
    }
}
