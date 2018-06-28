package com.antondevs.apps.githubbrowser.data.entries;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by Anton on 6/24/18.
 */
@Entity(tableName = "user")
public class UserEntry {
    
    @PrimaryKey(autoGenerate = true)
    private int mId;

    private String login;

    private int followers;
    private int following;

    public UserEntry() {

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
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
}
