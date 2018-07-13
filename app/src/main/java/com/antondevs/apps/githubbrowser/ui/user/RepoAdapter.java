package com.antondevs.apps.githubbrowser.ui.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;

import java.util.ArrayList;

/**
 * Created by Anton on 7/8/18.
 */
public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private static final String LOGTAG = RepoAdapter.class.getSimpleName();

    public interface RepoAdapterClickListener {

        void onRepoItemClick(String itemName);
    }

    private RepoAdapter.RepoAdapterClickListener clickListener;

    private ArrayList<String> repoList;

    public RepoAdapter(ArrayList<String> repoList, RepoAdapter.RepoAdapterClickListener clickListener) {
        this.repoList = repoList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RepoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_view_user_repos_list,
                parent, false);

        return new RepoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoAdapter.ViewHolder holder, int position) {

        holder.itemTextView.setText(repoList.get(position));

    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    public void swapRepoList(ArrayList<String> newRepoList) {
        repoList = newRepoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            itemTextView = (TextView) view.findViewById(R.id.item_view_repo_tv);

        }

        @Override
        public void onClick(View view) {
            Log.d(LOGTAG, "ViewHolder.onClick()");
            clickListener.onRepoItemClick(repoList.get(getAdapterPosition()));
        }

    }
}
