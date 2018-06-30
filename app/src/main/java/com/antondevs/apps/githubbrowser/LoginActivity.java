package com.antondevs.apps.githubbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antondevs.apps.githubbrowser.data.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.data.entries.UserEntry;
import com.antondevs.apps.githubbrowser.data.entries.RepoEntry;
import com.antondevs.apps.githubbrowser.utilities.DatabaseUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private GitHubBrowserDatabase mAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAppDatabase = GitHubBrowserDatabase.getDatabaseInstance(getApplicationContext());

        writeDataToDatabase();

        
    }


    public void writeDataToDatabase() {

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    mAppDatabase.userDao().insertUser(DatabaseUtils.generateUser());
                }
            }
        });
    }
}
