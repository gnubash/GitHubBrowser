package com.antondevs.apps.githubbrowser.ui.search;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;

/**
 * Created by Anton.
 */
public class SearchPresenterImp implements SearchContract.Presenter,
        MainStorage.SearchListener {

    private static final String LOGTAG = SearchPresenterImp.class.getSimpleName();

    private SearchContract.View view;
    private MainStorage storage;
    private boolean hasMoreResults;

    public SearchPresenterImp(SearchContract.View view, MainStorage storage) {
        this.view = view;
        this.storage = storage;
    }

    @Override
    public void searchUser(String userName) {
        Log.d(LOGTAG, "searchUser = " + userName);
        view.showLoading();
        storage.queryUsers(this, userName);
    }

    @Override
    public void searchContributors(String repoName) {
        Log.d(LOGTAG, "searchContributors = " + repoName);
        view.showLoading();
        storage.queryContributors(this, repoName);
    }

    @Override
    public void searchFollowers(String userName) {
        Log.d(LOGTAG, "searchFollowers = " + userName);
        view.showLoading();
        storage.queryFollowers(this, userName);
    }

    @Override
    public void searchFollowing(String userName) {
        Log.d(LOGTAG, "searchFollowing = " + userName);
        view.showLoading();
        storage.queryFollowing(this, userName);
    }

    @Override
    public void onNoResultsFound() {
        view.showViews();
        view.showNoResultsView();
    }

    @Override
    public void onSearchSuccess(List<UserEntry> userList) {
        Log.d(LOGTAG, "onSearchSuccess");
        hasMoreResults = true;
        view.setSearchResult(userList);
        view.showViews();
    }

    @Override
    public void onNoMoreResults() {
        Log.d(LOGTAG, "onNoMoreResults");
        hasMoreResults = false;
        view.showNoMoreSearchResults();
    }

    @Override
    public void userScrollToBottom() {
        Log.d(LOGTAG, "userScrollToBottom");
        if (hasMoreResults) {
            view.showLoadingMoreResults();
            storage.loadMoreSearchResults(this);
        }
    }
}
