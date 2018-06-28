package com.antondevs.apps.githubbrowser.data.entries;

import java.util.List;

/**
 * Created by Anton on 6/24/18.
 */
public class UserEntry {

    private int mId;

    private String mLoginName;

    private int mFollowers;
    private int mFollowing;

    private List<String> mOwnedRepos;
    private List<String> mStarredRepos;

    public UserEntry(){

    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmLoginName() {
        return mLoginName;
    }

    public void setmLoginName(String mLoginName) {
        this.mLoginName = mLoginName;
    }

    public int getmFollowers() {
        return mFollowers;
    }

    public void setmFollowers(int mFollowers) {
        this.mFollowers = mFollowers;
    }

    public int getmFollowing() {
        return mFollowing;
    }

    public void setmFollowing(int mFollowing) {
        this.mFollowing = mFollowing;
    }

    public List<String> getmOwnedRepos() {
        return mOwnedRepos;
    }

    public void setmOwnedRepos(List<String> mOwnedRepos) {
        this.mOwnedRepos = mOwnedRepos;
    }

    public List<String> getmStarredRepos() {
        return mStarredRepos;
    }

    public void setmStarredRepos(List<String> mStarredRepos) {
        this.mStarredRepos = mStarredRepos;
    }
}
