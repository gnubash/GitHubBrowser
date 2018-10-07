package com.antondevs.apps.githubbrowser.ui.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;

import java.util.List;

/**
 * Created by Anton.
 */
public class UserReposRecyclerViewAdapter extends RecyclerView.Adapter<UserReposRecyclerViewAdapter.ViewHolder> {

    private static final String LOGTAG = UserReposRecyclerViewAdapter.class.getSimpleName();

    public interface UserReposAdapterClickListener {

        void onUserItemCLick(String itemName);
    }

    private UserReposRecyclerViewAdapter.UserReposAdapterClickListener clickListener;

    private List<String> repoNamesList;

    public UserReposRecyclerViewAdapter(List<String> userRepoList, UserReposRecyclerViewAdapter.UserReposAdapterClickListener clickListener) {
        repoNamesList = userRepoList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserReposRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_view_user_repos,
                parent, false);

        return new UserReposRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReposRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.itemTextView.setText(repoNamesList.get(position));

    }

    @Override
    public int getItemCount() {
        return repoNamesList.size();
    }

    public void swapReposList(List<String> newRepoList) {
        repoNamesList = newRepoList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            itemTextView = view.findViewById(R.id.item_view_user_repos_tv);

        }

        @Override
        public void onClick(View view) {
            Log.d(LOGTAG, "ViewHolder.onClick");
            clickListener.onUserItemCLick(repoNamesList.get(getAdapterPosition()));
        }

    }
}
