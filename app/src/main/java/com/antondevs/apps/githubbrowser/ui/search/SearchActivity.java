package com.antondevs.apps.githubbrowser.ui.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.antondevs.apps.githubbrowser.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchContract.View,
        UserSearchAdapter.UserSearchAdapterClickListener {

    private static final String LOGTAG = SearchActivity.class.getSimpleName();

    private UserSearchAdapter adapter;

    private RecyclerView usersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        usersRecyclerView = (RecyclerView) findViewById(R.id.search_user_recycler_view);
        usersRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setSearchResult(List<String> userList) {
        if (adapter == null) {
            adapter = new UserSearchAdapter((ArrayList<String>) userList, this);
            usersRecyclerView.setAdapter(adapter);
        }
        else {
            adapter.swapUserList((ArrayList<String>) userList);
        }
    }

    @Override
    public void showNoResultsView() {
        if (!(adapter == null)) {
            usersRecyclerView.setAdapter(null);
        }
    }

    @Override
    public void onUserItemCLick(String itemName) {
        Log.d(LOGTAG, "onUserItemCLick(String)" + itemName);
        Toast.makeText(this, itemName, Toast.LENGTH_SHORT).show();
    }
}
