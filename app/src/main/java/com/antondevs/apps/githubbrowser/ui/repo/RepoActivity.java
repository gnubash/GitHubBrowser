package com.antondevs.apps.githubbrowser.ui.repo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.databinding.ActivityRepoBinding;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

public class RepoActivity extends AppCompatActivity implements RepoContract.View {

    private static final String LOGTAG = RepoActivity.class.getSimpleName();

    private ActivityRepoBinding binding;

    private RepoContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo);
//        setContentView(R.layout.activity_repo);

        Log.d(LOGTAG, "onCreate()");
        String repoName = "";

        if (getIntent() != null && getIntent().hasExtra(UserActivity.INTENT_EXTRA_REPO_NAME_KEY)) {
            repoName = getIntent().getStringExtra(UserActivity.INTENT_EXTRA_REPO_NAME_KEY);
            presenter = new RepoPresenterImp(repoName, this);
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
                // TODO Start new search for user contributors to this library
            }
        });

    }

    @Override
    public void setOwner(String ownerName) {
        binding.repoOwnerNameTextView.setText(ownerName);
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
                // TODO Star the current repository
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