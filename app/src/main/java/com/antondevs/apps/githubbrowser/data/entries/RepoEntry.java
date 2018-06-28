package com.antondevs.apps.githubbrowser.data.entries;

/**
 * Created by Anton on 6/24/18.
 */

public class RepoEntry {

    private int mId;

    private String mOwnerName;
    private String mRepoName;

    private int mForksCount;
    private int mStarCount;

    public RepoEntry() {

    }

    public String getmOwnerName() {
        return mOwnerName;
    }

    public void setmOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getmRepoName() {
        return mRepoName;
    }

    public void setmRepoName(String mRepoName) {
        this.mRepoName = mRepoName;
    }

    public int getmForksCount() {
        return mForksCount;
    }

    public void setmForksCount(int mForksCount) {
        this.mForksCount = mForksCount;
    }

    public int getmStarCount() {
        return mStarCount;
    }

    public void setmStarCount(int mStarCount) {
        this.mStarCount = mStarCount;
    }
}
