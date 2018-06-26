package com.antondevs.apps.githubbrowser.data.entries;

/**
 * Created by Anton on 6/24/18.
 */

public class RepoEntry {

    private int mId;

    private String mOwnerName;
    private String mRepoName;

    private String mContributorsUrl;
    private String mOwnerAvatarUrl;
    private String mBranchesUrl;

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

    public String getmContributorsUrl() {
        return mContributorsUrl;
    }

    public void setmContributorsUrl(String mContributorsUrl) {
        this.mContributorsUrl = mContributorsUrl;
    }

    public String getmOwnerAvatarUrl() {
        return mOwnerAvatarUrl;
    }

    public void setmOwnerAvatarUrl(String mOwnerAvatarUrl) {
        this.mOwnerAvatarUrl = mOwnerAvatarUrl;
    }

    public String getmBranchesUrl() {
        return mBranchesUrl;
    }

    public void setmBranchesUrl(String mBranchesUrl) {
        this.mBranchesUrl = mBranchesUrl;
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
