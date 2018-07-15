package com.antondevs.apps.githubbrowser.ui.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.antondevs.apps.githubbrowser.R;
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

    private UserSearchAdapter adapter;

    private RecyclerView usersRecyclerView;

    private SearchContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usersRecyclerView = (RecyclerView) findViewById(R.id.search_user_recycler_view);
        usersRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(layoutManager);

        presenter = new SearchPresenterImp(this);
        checkIntent();
    }

    @Override
    public void setSearchResult(List<String> userList) {
        if (adapter == null) {
            adapter = new UserSearchAdapter((ArrayList<String>) userList, this);
            usersRecyclerView.setAdapter(adapter);
        }
        else {
            adapter.swapUserList((ArrayList<String>) userList);
        }
    }

    @Override
    public void showNoResultsView() {
        if (!(adapter == null)) {
            usersRecyclerView.setAdapter(null);
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
