package com.antondevs.apps.githubbrowser.ui.search;

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
 * Created by Anton.
 */
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {

    private static final String LOGTAG = UserSearchAdapter.class.getSimpleName();

    public interface UserSearchAdapterClickListener {

        void onUserItemCLick(String itemName);
    }

    private UserSearchAdapter.UserSearchAdapterClickListener clickListener;

    private ArrayList<String> userList;

    public UserSearchAdapter(ArrayList<String> userList, UserSearchAdapter.UserSearchAdapterClickListener clickListener) {
        this.userList = userList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_view_user_repos_list,
                parent, false);

        return new UserSearchAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchAdapter.ViewHolder holder, int position) {

        holder.itemTextView.setText(userList.get(position));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void swapUserList(ArrayList<String> newUserList) {
        userList = newUserList;
        notifyDataSetChanged();
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
            clickListener.onUserItemCLick(userList.get(getAdapterPosition()));
        }

    }
}
