package com.antondevs.apps.githubbrowser.ui.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;
import com.antondevs.apps.githubbrowser.ui.repo.RepoActivity;
import com.antondevs.apps.githubbrowser.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserContract.UserView,
        RepoAdapter.RepoAdapterClickListener {

    private static final String LOGTAG = UserActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_REPO_NAME_KEY = "repo_name";

    private TextView usernameTextView;
    private Button followersButton;
    private Button follwingButton;
    private Button ownReposButton;
    private Button starredReposButton;
    private RecyclerView reposRecyclerView;
    private UserContract.UserPresenter userPresenter;
    private RepoAdapter repoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Log.d(LOGTAG, "onCreate()");

        usernameTextView = (TextView) findViewById(R.id.user_login_name_text_view);
        followersButton = (Button) findViewById(R.id.user_followers_button);
        follwingButton = (Button) findViewById(R.id.user_following_button);
        ownReposButton = (Button) findViewById(R.id.user_owned_repos_btn);
        starredReposButton = (Button) findViewById(R.id.user_starred_repos_btn);
        reposRecyclerView = (RecyclerView) findViewById(R.id.user_repos_recycler);

        reposRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        reposRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent != null) {
            String exra = intent.getStringExtra(LoginActivity.INTENT_EXTRA_USER_LOGIN_KEY);
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

        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Maybe obtain the User's login name from the view instead of calling presenter.
                Intent followersIntentSearch = new Intent(UserActivity.this, SearchActivity.class);
                followersIntentSearch.putExtra(SearchActivity.EXTRA_SEARCH_USER_FOLLOWERS, userPresenter.getUserLoginName());
                startActivity(followersIntentSearch);
            }
        });

        follwingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Find how to remove duplicated code in both OnClick methods.
                Intent followingIntentSearch = new Intent(UserActivity.this, SearchActivity.class);
                followingIntentSearch.putExtra(SearchActivity.EXTRA_SEARCH_USER_FOLLOWING, userPresenter.getUserLoginName());
                startActivity(followingIntentSearch);
            }
        });

        ownReposButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPresenter.getOwnedRepos();
            }
        });

        starredReposButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPresenter.getStarredRepos();
            }
        });
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
        if (repoAdapter == null) {
            Log.d(LOGTAG, "setReposList() if statement. repoAdapter == null");
            repoAdapter = new RepoAdapter((ArrayList<String>) repoEntryList, this);
            reposRecyclerView.setAdapter(repoAdapter);
        }
        else {
            repoAdapter.swapRepoList((ArrayList<String>) repoEntryList);
        }

    }

    @Override
    public void onRepoItemClick(String itemName) {
        Log.d(LOGTAG, "onRepoItemClick(String)" + itemName);
        Toast.makeText(this, itemName, Toast.LENGTH_SHORT).show();
        Intent repoActivityIntent = new Intent(this, RepoActivity.class);
        repoActivityIntent.putExtra(INTENT_EXTRA_REPO_NAME_KEY, itemName);
        startActivity(repoActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_action_search:
                // TODO Start Search View when this is selected
                return true;
            case R.id.menu_action_logout:
                // TODO Should call presenter to logout(finish activity directly after setting logged out status
                return true;
            case R.id.menu_action_home:
                // TODO Go to logged user
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
