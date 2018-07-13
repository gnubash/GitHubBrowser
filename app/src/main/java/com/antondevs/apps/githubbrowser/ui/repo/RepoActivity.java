package com.antondevs.apps.githubbrowser.ui.repo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.databinding.ActivityRepoBinding;

public class RepoActivity extends AppCompatActivity {

    private static final String LOGTAG = RepoActivity.class.getSimpleName();

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        ActivityRepoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_repo);
    }
}
