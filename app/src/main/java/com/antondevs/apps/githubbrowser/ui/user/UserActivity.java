package com.antondevs.apps.githubbrowser.ui.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.MainStorageImp;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelperImp;
import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.databinding.ActivityUserBinding;
import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;
import com.antondevs.apps.githubbrowser.ui.repo.RepoActivity;
import com.antondevs.apps.githubbrowser.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserContract.UserView,
        RepoAdapter.RepoAdapterClickListener {

    private static final String LOGTAG = UserActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_REPO_NAME_KEY = "repo_name";

    private ActivityUserBinding binding;

    private UserContract.UserPresenter userPresenter;
    private RepoAdapter repoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        Log.d(LOGTAG, "onCreate()");


        binding.userReposRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.userReposRecycler.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent != null) {
            String exra = intent.getStringExtra(LoginActivity.INTENT_EXTRA_USER_LOGIN_KEY);

            GitHubBrowserDatabase database = GitHubBrowserDatabase.getDatabaseInstance(this);
            DatabaseHelper databaseHelper = new DatabaseHelperImp(database);

            MainStorage storage = MainStorageImp.getInstance();
            storage.setDatabaseHelper(databaseHelper);

            userPresenter = new UserPresenterImp(exra, this, storage);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(exra);
            }
            userPresenter.loadPresenter();
        }
        else {
            throw new RuntimeException("UserActivity must be started with IntentExtra");
        }

        binding.userFollowersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followersIntentSearch = new Intent(UserActivity.this, SearchActivity.class);
                followersIntentSearch.putExtra(SearchActivity.EXTRA_SEARCH_USER_FOLLOWERS, userPresenter.getUserLoginName());
                startActivity(followersIntentSearch);
            }
        });

        binding.userFollowingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent followingIntentSearch = new Intent(UserActivity.this, SearchActivity.class);
                followingIntentSearch.putExtra(SearchActivity.EXTRA_SEARCH_USER_FOLLOWING, userPresenter.getUserLoginName());
                startActivity(followingIntentSearch);
            }
        });

        binding.userOwnedReposBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPresenter.getOwnedRepos();
            }
        });

        binding.userStarredReposBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPresenter.getStarredRepos();
            }
        });
    }

    @Override
    public void setUserName(String name) {
        binding.userLoginNameTextView.setText(name);
    }

    @Override
    public void setFollowers(String followersNumber) {
        binding.userFollowersButton.append(followersNumber);
    }

    @Override
    public void setFollowing(String followingNumber) {
        binding.userFollowingButton.append(followingNumber);
    }

    @Override
    public void setReposList(List<String> repoEntryList) {
        if (repoAdapter == null) {
            Log.d(LOGTAG, "setReposList() if statement. repoAdapter == null");
            repoAdapter = new RepoAdapter((ArrayList<String>) repoEntryList, this);
            binding.userReposRecycler.setAdapter(repoAdapter);
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
                // Start Search View when this is selected
                return true;
            case R.id.menu_action_logout:
                //  Should call presenter to logout(finish activity directly after setting logged out status
                return true;
            case R.id.menu_action_home:
                //  Go to logged user
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoading() {
        binding.container.setVisibility(View.INVISIBLE);
        binding.progressBarFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.container.setVisibility(View.VISIBLE);
        binding.progressBarFrame.setVisibility(View.GONE);
    }
}
