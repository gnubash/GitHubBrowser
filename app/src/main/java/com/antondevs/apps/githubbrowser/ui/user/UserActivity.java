package com.antondevs.apps.githubbrowser.ui.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.data.database.RepoEntry;

import java.util.List;

public class UserActivity extends AppCompatActivity implements UserContract.UserView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    public void setUserName(String name) {

    }

    @Override
    public void setFollowers(String followersNumber) {

    }

    @Override
    public void setFollowing(String followingNumber) {

    }

    @Override
    public void setReposList(List<RepoEntry> repoEntryList) {

    }
}
