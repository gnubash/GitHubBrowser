package com.antondevs.apps.githubbrowser.data.entries;

import java.util.List;

/**
 * Created by Anton on 6/24/18.
 */
public class UserEntry {

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
