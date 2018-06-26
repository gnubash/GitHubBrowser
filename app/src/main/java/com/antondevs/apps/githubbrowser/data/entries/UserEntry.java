package com.antondevs.apps.githubbrowser.data.entries;

/**
 * Created by Anton on 6/24/18.
 */
public class UserEntry {

    private int mId;

    private String mLoginName;

    private String mAvatarUrl;
    private String mFollowersUrl;
    private String mFollowingUrl;

    private String mStarredUrl;
    private String mReposUrl;

    public UserEntry(){

    }

    public String getmLoginName() {
        return mLoginName;
    }

    public void setmLoginName(String mLoginName) {
        this.mLoginName = mLoginName;
    }

    public String getmAvatarUrl() {
        return mAvatarUrl;
    }

    public void setmAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getmFollowersUrl() {
        return mFollowersUrl;
    }

    public void setmFollowersUrl(String mFollowersUrl) {
        this.mFollowersUrl = mFollowersUrl;
    }

    public String getmFollowingUrl() {
        return mFollowingUrl;
    }

    public void setmFollowingUrl(String mFollowingUrl) {
        this.mFollowingUrl = mFollowingUrl;
    }

    public String getmStarredUrl() {
        return mStarredUrl;
    }

    public void setmStarredUrl(String mStarredUrl) {
        this.mStarredUrl = mStarredUrl;
    }

    public String getmReposUrl() {
        return mReposUrl;
    }

    public void setmReposUrl(String mReposUrl) {
        this.mReposUrl = mReposUrl;
    }
}
