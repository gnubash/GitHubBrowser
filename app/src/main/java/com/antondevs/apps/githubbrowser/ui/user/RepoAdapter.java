package com.antondevs.apps.githubbrowser.ui.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;

import java.util.ArrayList;

/**
 * Created by Anton on 7/8/18.
 */
public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    private ArrayList<String> mRepoList;

    public RepoAdapter(ArrayList<String> mRepoList) {
        this.mRepoList = mRepoList;
    }

    @NonNull
    @Override
    public RepoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        TextView itemView = (TextView) inflater.inflate(R.layout.item_view_user_repos_list,
                parent, false);

        return new RepoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoAdapter.ViewHolder holder, int position) {

        holder.itemTextView.setText(mRepoList.get(position));

    }

    @Override
    public int getItemCount() {
        return mRepoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTextView;

        public ViewHolder(TextView view) {
            super(view);
            itemTextView = view;

        }
    }
}
