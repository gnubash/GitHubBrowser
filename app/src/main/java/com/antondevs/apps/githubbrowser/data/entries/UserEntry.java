package com.antondevs.apps.githubbrowser.data.entries;

/**
 * Created by Anton on 6/24/18.
 */
public class UserEntry {

    private int mId;

    private String mLoginName;

    private String mAvatarUrl;

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
    
}
