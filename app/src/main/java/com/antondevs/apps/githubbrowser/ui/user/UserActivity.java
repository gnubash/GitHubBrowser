package com.antondevs.apps.githubbrowser.ui.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserContract.UserView,
        RepoAdapter.RepoAdapterClickListener {

    private static final String LOGTAG = UserActivity.class.getSimpleName();

    private TextView usernameTextView;
    private Button followersButton;
    private Button follwingButton;
    private RecyclerView reposRecyclerView;
    private UserContract.UserPresenter userPresenter;
    private RepoAdapter repoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        usernameTextView = (TextView) findViewById(R.id.user_login_name_text_view);
        followersButton = (Button) findViewById(R.id.user_followers_button);
        follwingButton = (Button) findViewById(R.id.user_following_button);
        reposRecyclerView = (RecyclerView) findViewById(R.id.user_repos_recycler);

        reposRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reposRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent != null) {
            String exra = intent.getStringExtra(LoginActivity.INTENT_EXTRA_KEY);
            userPresenter = new UserPresenterImp(exra, this);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(exra);
            }
            userPresenter.loadPresenter();
        }
        else {
            userPresenter = new UserPresenterImp("Something is wrong!", this);
            userPresenter.loadPresenter();
        }
    }

    @Override
    public void setUserName(String name) {
        usernameTextView.setText(name);
    }

    @Override
    public void setFollowers(String followersNumber) {
        followersButton.append(followersNumber);
    }

    @Override
    public void setFollowing(String followingNumber) {
        follwingButton.append(followingNumber);
    }

    @Override
    public void setReposList(List<String> repoEntryList) {
        repoAdapter = new RepoAdapter((ArrayList<String>) repoEntryList, this);
        reposRecyclerView.setAdapter(repoAdapter);
    }

    @Override
    public void onRepoItemClick(String itemName) {
        Log.d(LOGTAG, "onRepoItemClick(String)" + itemName);
        Toast.makeText(this, itemName, Toast.LENGTH_SHORT).show();
    }
}
