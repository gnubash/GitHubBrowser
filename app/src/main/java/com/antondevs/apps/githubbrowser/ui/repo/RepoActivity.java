package com.antondevs.apps.githubbrowser.ui.repo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.databinding.ActivityRepoBinding;

public class RepoActivity extends AppCompatActivity implements RepoContract.View {

    private static final String LOGTAG = RepoActivity.class.getSimpleName();

    private ActivityRepoBinding binding;

    private RepoContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo);
//        setContentView(R.layout.activity_repo);


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
}
