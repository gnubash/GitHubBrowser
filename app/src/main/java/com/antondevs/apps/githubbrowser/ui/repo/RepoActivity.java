package com.antondevs.apps.githubbrowser.ui.repo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.MainStorageImp;
import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.databinding.ActivityRepoBinding;
import com.antondevs.apps.githubbrowser.ui.AbsGitHubActivity;
import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;
import com.antondevs.apps.githubbrowser.ui.search.SearchActivity;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

public class RepoActivity extends AbsGitHubActivity implements RepoContract.View {

    private static final String LOGTAG = RepoActivity.class.getSimpleName();

    private ActivityRepoBinding binding;

    private RepoContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo);

        Log.d(LOGTAG, "onCreate()");
        String repoName = "";

        if (getIntent() != null && getIntent().hasExtra(UserActivity.INTENT_EXTRA_REPO_NAME_KEY)) {
            repoName = getIntent().getStringExtra(UserActivity.INTENT_EXTRA_REPO_NAME_KEY);

            GitHubBrowserDatabase database = GitHubBrowserDatabase.getDatabaseInstance(this);
            MainStorage storage = MainStorageImp.getInstance();
            storage.setDatabaseHelper(database);

            presenter = new RepoPresenterImp(repoName, this, storage);
        }
        else {
            throw new RuntimeException("RepoActivity must be started with IntentExtra.");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(repoName);
        }

        presenter.loadPresenter();

        binding.repoContributorsButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchContributorsIntent = new Intent(RepoActivity.this, SearchActivity.class);
                searchContributorsIntent.putExtra(SearchActivity.EXTRA_SEARCH_REPO_CONTRIBUTORS, presenter.getRepoName());
                startActivity(searchContributorsIntent);
            }
        });

        binding.repoOwnerImageWrapperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(RepoActivity.this, UserActivity.class);
                userIntent.putExtra(LoginActivity.INTENT_EXTRA_USER_LOGIN_KEY,
                        binding.repoOwnerNameTextView.getText());
                startActivity(userIntent);
            }
        });

    }

    @Override
    public void setOwnerName(String ownerName) {
        binding.repoOwnerNameTextView.setText(ownerName);
    }

    @Override
    public void setRepoName(String repoName) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(repoName);
        }
    }

    @Override
    public void setCommits(String commitsNumber) {
        binding.repoCommitsTextView.append(commitsNumber);
    }

    @Override
    public void setBranches(String branchesNumber) {
        binding.repoBranchesTextView.append(branchesNumber);
    }

    @Override
    public void setReleases(String releasesNumber) {
        binding.repoReleasesTextView.append(releasesNumber);
    }

    @Override
    public void setContributors(String contributorsNumber) {
        binding.repoContributorsButtonTextView.append(contributorsNumber);
    }

    @Override
    public void setStar(String starNumber) {
        binding.repoStarredTextView.append(starNumber);
    }

    @Override
    public void setFork(String forkNumber) {
        binding.repoForkTextView.append(forkNumber);
    }

    @Override
    public void showLoading() {
        binding.repoViewContainer.setVisibility(View.INVISIBLE);
        binding.progressBarFrame.setVisibility(View.VISIBLE);
        binding.errorView.errorTvRoot.setVisibility(View.GONE);
    }

    @Override
    public void showViews() {
        binding.repoViewContainer.setVisibility(View.VISIBLE);
        binding.progressBarFrame.setVisibility(View.GONE);
        binding.errorView.errorTvRoot.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        binding.repoViewContainer.setVisibility(View.GONE);
        binding.progressBarFrame.setVisibility(View.GONE);
        binding.errorView.errorTv.setText(getString(R.string.no_data_available_with_network));
        binding.errorView.errorTvRoot.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_repo_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
            case R.id.menu_action_star:
                //  Star the current repository
                return true;
            case R.id.menu_action_logout:
                presenter.logout();
                return true;
            case R.id.menu_action_home:
                presenter.home();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
