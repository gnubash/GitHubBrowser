package com.antondevs.apps.githubbrowser.ui.search;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.databinding.ActivitySearchBinding;
import com.antondevs.apps.githubbrowser.ui.AbsGitHubActivity;
import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

public class SearchActivity extends AbsGitHubActivity implements SearchContract.View,
        UserSearchAdapter.UserSearchAdapterClickListener {

    private static final String LOGTAG = SearchActivity.class.getSimpleName();

    public static final String EXTRA_SEARCH_REPO_CONTRIBUTORS = "repo_name_contributors";
    public static final String EXTRA_SEARCH_USER_FOLLOWERS = "user_login_followers";
    public static final String EXTRA_SEARCH_USER_FOLLOWING = "user_login_following";

    private ActivitySearchBinding binding;

    private UserSearchAdapter adapter;

    private SearchContract.Presenter presenter;

    private boolean requestFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGTAG, "onCreate");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.search_title));
        }

        binding.searchUserRecyclerView.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.searchUserRecyclerView.setLayoutManager(layoutManager);

        GitHubBrowserDatabase database = GitHubBrowserDatabase.getDatabaseInstance(this);
        MainStorage storage = MainStorageImp.getInstance();
        storage.setDatabaseHelper(database);

        presenter = new SearchPresenterImp(this, storage);
        checkIntent();

        binding.searchUserRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                boolean reachedBottom = recyclerView.canScrollVertically(1);
                if (!reachedBottom) {
                    super.onScrolled(recyclerView, dx, dy);
                    return;
                }

                if (layoutManager.findLastVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    Log.d(LOGTAG, "onScrolled lastVisible = " +
                            layoutManager.findLastVisibleItemPosition() + " itemCount " +
                            layoutManager.getItemCount());
                    presenter.userScrollToBottom();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void resultsLoaded() {
        Log.d(LOGTAG, "resultsLoaded");
        if (adapter == null) {
            Log.d(LOGTAG, "resultsLoaded adapter == null");
            adapter = new UserSearchAdapter(presenter, this);
            binding.searchUserRecyclerView.setAdapter(adapter);
        }
        else {
            Log.d(LOGTAG, "resultsLoaded adapter != null");
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showNoResultsView() {
        Log.d(LOGTAG, "showNoResultsView");
        if (!(adapter == null)) {
            binding.searchUserRecyclerView.setAdapter(null);
        }
        binding.loginProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onUserItemCLick(String itemName) {
        Log.d(LOGTAG, "onUserItemCLick " + itemName);
        Toast.makeText(this, itemName, Toast.LENGTH_SHORT).show();
        Intent userActivityIntent = new Intent(this, UserActivity.class);
        userActivityIntent.putExtra(LoginActivity.INTENT_EXTRA_USER_LOGIN_KEY, itemName);
        startActivity(userActivityIntent);
    }

    @Override
    public void showLoading() {
        Log.d(LOGTAG, "showLoading");
        binding.loginProgressBar.setVisibility(View.VISIBLE);
        binding.searchViewContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showViews() {
        Log.d(LOGTAG, "showViews");
        binding.loginProgressBar.setVisibility(View.GONE);
        binding.searchViewContainer.setVisibility(View.VISIBLE);
        binding.loadingMoreProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoMoreSearchResults() {
        Log.d(LOGTAG, "showNoMoreSearchResults");
        binding.loadingMoreProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, "No more results", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingMoreResults() {
        Log.d(LOGTAG, "showLoadingMoreResults");
        binding.loadingMoreProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_action_search_search_screen);
        SearchView searchView = (SearchView) searchItem.getActionView();

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

        if (requestFocus) {
            searchView.setIconified(false);
            searchView.requestFocus();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
            case R.id.menu_action_search:
                return true;
            case R.id.menu_action_logout_search_screen:
                presenter.logout();
                return true;
            case R.id.menu_action_home_search_screen:
                presenter.home();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_SEARCH_USER_FOLLOWERS)) {
            Log.d(LOGTAG, "checkIntent " + EXTRA_SEARCH_USER_FOLLOWERS);
            presenter.searchFollowers(intent.getStringExtra(EXTRA_SEARCH_USER_FOLLOWERS));
        }
        else if (intent.hasExtra(EXTRA_SEARCH_USER_FOLLOWING)) {
            Log.d(LOGTAG, "checkIntent " + EXTRA_SEARCH_USER_FOLLOWING);
            presenter.searchFollowing(intent.getStringExtra(EXTRA_SEARCH_USER_FOLLOWING));
        }
        else if (intent.hasExtra(EXTRA_SEARCH_REPO_CONTRIBUTORS)) {
            Log.d(LOGTAG, "checkIntent " + EXTRA_SEARCH_REPO_CONTRIBUTORS);
            presenter.searchContributors(intent.getStringExtra(EXTRA_SEARCH_REPO_CONTRIBUTORS));
        }
        else {
            Log.d(LOGTAG, "checkIntent 'default'");
            requestFocus = true;
        }
    }
}
