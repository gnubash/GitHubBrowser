package com.antondevs.apps.githubbrowser.ui.repo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.MainStorageImp;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelperImp;
import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.databinding.ActivityRepoBinding;
import com.antondevs.apps.githubbrowser.ui.search.SearchActivity;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

public class RepoActivity extends AppCompatActivity implements RepoContract.View {

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
            DatabaseHelper databaseHelper = new DatabaseHelperImp(database);
            MainStorage storage = MainStorageImp.getInstance();
            storage.setDatabaseHelper(databaseHelper);

            presenter = new RepoPresenterImp(repoName, this, storage);
        }
        else {
            throw new RuntimeException("RepoActivity must be started with IntentExtra.");
        }

        if (getSupportActionBar() != null) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_repo_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.menu_action_star:
                //  Star the current repository
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
}
