package com.antondevs.apps.githubbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antondevs.apps.githubbrowser.data.GitHubBrowserDatabase;

public class LoginActivity extends AppCompatActivity {

    private GitHubBrowserDatabase mAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAppDatabase = GitHubBrowserDatabase.getDatabaseInstance(this);
    }
}
