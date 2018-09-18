package com.antondevs.apps.githubbrowser.ui.search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.databinding.ActivitySearchBinding;
import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchContract.View,
        UserSearchAdapter.UserSearchAdapterClickListener {

    private static final String LOGTAG = SearchActivity.class.getSimpleName();

    public static final String EXTRA_SEARCH_REPO_CONTRIBUTORS = "repo_name_contributors";
    public static final String EXTRA_SEARCH_USER_FOLLOWERS = "user_login_followers";
    public static final String EXTRA_SEARCH_USER_FOLLOWING = "user_login_following";

    private ActivitySearchBinding binding;

    private UserSearchAdapter adapter;

    private SearchContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.searchUserRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.searchUserRecyclerView.setLayoutManager(layoutManager);

        GitHubBrowserDatabase database = GitHubBrowserDatabase.getDatabaseInstance(this);
        DatabaseHelper databaseHelper = new DatabaseHelperImp(database);
        MainStorage storage = MainStorageImp.getInstance();
        storage.setDatabaseHelper(databaseHelper);

        presenter = new SearchPresenterImp(this, storage);
        checkIntent();
    }

    @Override
    public void setSearchResult(List<UserEntry> userList) {
        if (adapter == null) {
            adapter = new UserSearchAdapter((ArrayList<UserEntry>) userList, this);
            binding.searchUserRecyclerView.setAdapter(adapter);
        }
        else {
            adapter.swapUserList((ArrayList<UserEntry>) userList);
        }
    }

    @Override
    public void showNoResultsView() {
        if (!(adapter == null)) {
            binding.searchUserRecyclerView.setAdapter(null);
        }
    }

    @Override
    public void onUserItemCLick(String itemName) {
        Log.d(LOGTAG, "onUserItemCLick(String)" + itemName);
        Toast.makeText(this, itemName, Toast.LENGTH_SHORT).show();
        Intent userActivityIntent = new Intent(this, UserActivity.class);
        userActivityIntent.putExtra(LoginActivity.INTENT_EXTRA_USER_LOGIN_KEY, itemName);
        startActivity(userActivityIntent);
    }

    @Override
    public void showLoading() {
        binding.loginProgressBar.setVisibility(View.VISIBLE);
        binding.searchViewContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showViews() {
        binding.loginProgressBar.setVisibility(View.GONE);
        binding.searchViewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoMoreSearchResults() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_action_search_search_screen);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
            case R.id.menu_action_search:
                //  Start Search View when this is selected
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

    private void checkIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_SEARCH_USER_FOLLOWERS)) {
            presenter.searchFollowers(intent.getStringExtra(EXTRA_SEARCH_USER_FOLLOWERS));
        }
        else if (intent.hasExtra(EXTRA_SEARCH_USER_FOLLOWING)) {
            presenter.searchFollowing(intent.getStringExtra(EXTRA_SEARCH_USER_FOLLOWING));
        }
        else if (intent.hasExtra(EXTRA_SEARCH_REPO_CONTRIBUTORS)) {
            presenter.searchContributors(intent.getStringExtra(EXTRA_SEARCH_REPO_CONTRIBUTORS));
        }
    }
}
