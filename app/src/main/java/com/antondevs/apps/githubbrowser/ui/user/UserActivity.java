package com.antondevs.apps.githubbrowser.ui.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.antondevs.apps.githubbrowser.databinding.ActivityUserBinding;
import com.antondevs.apps.githubbrowser.ui.AbsGitHubActivity;
import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;
import com.antondevs.apps.githubbrowser.ui.repo.RepoActivity;
import com.antondevs.apps.githubbrowser.ui.search.SearchActivity;

import java.util.List;

public class UserActivity extends AbsGitHubActivity implements UserContract.UserView,
        UserReposFragment.UserReposClickListener, UserReposFragment.ReposListScrollListener {

    private static final String LOGTAG = UserActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_REPO_NAME_KEY = "repo_name";

    private ActivityUserBinding binding;

    private UserContract.UserPresenter userPresenter;

    private UserReposPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        Log.d(LOGTAG, "onCreate()");

        fragmentPagerAdapter = new UserReposPagerAdapter(getSupportFragmentManager());

        binding.userViewPager.setAdapter(fragmentPagerAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            String exra = intent.getStringExtra(LoginActivity.INTENT_EXTRA_USER_LOGIN_KEY);
            Log.d(LOGTAG, "onCreate() intent != null " + exra);

            GitHubBrowserDatabase database = GitHubBrowserDatabase.getDatabaseInstance(this);
            MainStorage storage = MainStorageImp.getInstance();
            storage.setDatabaseHelper(database);

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

    }

    @Override
    public void setUserName(String name) {
        binding.userLoginNameTextView.setText(name);
    }

    @Override
    public void setFollowers(String followersNumber) {
        binding.userFollowersButton.setText(String.format(getString(R.string.followers_button_text), followersNumber));
    }

    @Override
    public void setFollowing(String followingNumber) {
        binding.userFollowingButton.setText(String.format(getString(R.string.following_button_text), followingNumber));
    }

    @Override
    public void setOwnedReposList(List<String> repoEntryList) {
        Log.d(LOGTAG, "setOwnedReposList");

        fragmentPagerAdapter.setOwnedReposList(repoEntryList);
    }

    @Override
    public void setStarredReposList(List<String> repoEntryList) {
        Log.d(LOGTAG, "setStarredReposList");

        fragmentPagerAdapter.setStarredReposList(repoEntryList);

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
                Intent searchActivityIntent = new Intent(UserActivity.this, SearchActivity.class);
                startActivity(searchActivityIntent);
                return true;
            case R.id.menu_action_logout:
                userPresenter.logout();
                return true;
            case R.id.menu_action_home:
                userPresenter.home();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoading() {
        binding.userViewContainer.setVisibility(View.INVISIBLE);
        binding.progressBarFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void showViews() {
        binding.errorView.errorTvRoot.setVisibility(View.GONE);
        binding.userViewContainer.setVisibility(View.VISIBLE);
        binding.progressBarFrame.setVisibility(View.GONE);
    }

    @Override
    public void onRepoSelected(String repoFullName) {
        onRepoItemClick(repoFullName);
    }

    @Override
    public void onScrollToBottom() {
        String currentPageTitile = String.valueOf(fragmentPagerAdapter.getPageTitle(binding.userViewPager.getCurrentItem()));
        if (currentPageTitile.equals(UserReposPagerAdapter.TAB_1_TITLE)) {
            Log.d(LOGTAG, "onScrollToBottom " + UserReposPagerAdapter.TAB_1_TITLE);
            userPresenter.scrollOwnedToBottom();
        }
        else {
            Log.d(LOGTAG, "onScrollToBottom " + UserReposPagerAdapter.TAB_2_TITLE);
            userPresenter.scrollStarredToBottom();
        }
    }

    @Override
    public void showNoData() {
        binding.userViewContainer.setVisibility(View.INVISIBLE);
        binding.progressBarFrame.setVisibility(View.INVISIBLE);
        binding.errorView.errorTv.setText(getString(R.string.no_data_available_with_network));
        binding.errorView.errorTvRoot.setVisibility(View.VISIBLE);
    }

    public void onRepoItemClick(String itemName) {
        Log.d(LOGTAG, "onRepoItemClick " + itemName);
        Toast.makeText(this, itemName, Toast.LENGTH_SHORT).show();
        Intent repoActivityIntent = new Intent(this, RepoActivity.class);
        repoActivityIntent.putExtra(INTENT_EXTRA_REPO_NAME_KEY, itemName);
        startActivity(repoActivityIntent);
    }
}
