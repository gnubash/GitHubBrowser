package com.antondevs.apps.githubbrowser.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;

/**
 * Created by Anton.
 */
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {

    private static final String LOGTAG = UserSearchAdapter.class.getSimpleName();

    public interface UserSearchAdapterClickListener {

        void onUserItemCLick(String itemName);
    }

    private UserSearchAdapter.UserSearchAdapterClickListener clickListener;

    private SearchContract.PresenterSearchResults presenterSearchResults;

    public UserSearchAdapter(SearchContract.PresenterSearchResults presenterSearchResults, UserSearchAdapter.UserSearchAdapterClickListener clickListener) {
        Log.d(LOGTAG, "UserSearchAdapter");
        this.presenterSearchResults = presenterSearchResults;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(LOGTAG, "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_view_user_search,
                parent, false);

        return new UserSearchAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchAdapter.ViewHolder holder, int position) {
        Log.d(LOGTAG, "onBindViewHolder");

        presenterSearchResults.bindViewToPosition(position, holder);

    }

    @Override
    public int getItemCount() {
        Log.d(LOGTAG, "getItemCount");
        return presenterSearchResults.getItemsCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            SearchContract.ViewSearchResultItem {

        TextView itemTextView;

        public ViewHolder(View view) {
            super(view);
            Log.d(LOGTAG, "ViewHolder");
            view.setOnClickListener(this);
            itemTextView = view.findViewById(R.id.item_view_user_login_tv);

        }

        @Override
        public void setLoginName(String loginName) {
            Log.d(LOGTAG, "ViewHolder.setLoginName " + loginName);
            itemTextView.setText(loginName);
        }

        @Override
        public void setImageUrl(String imageUrl) {
            Log.d(LOGTAG, "ViewHolder.setImageUrl " + imageUrl);
        }

        @Override
        public void onClick(View view) {
            Log.d(LOGTAG, "ViewHolder.onClick");
            clickListener.onUserItemCLick(itemTextView.getText().toString());
        }

    }
}
