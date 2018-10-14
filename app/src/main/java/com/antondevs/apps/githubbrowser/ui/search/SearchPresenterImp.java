package com.antondevs.apps.githubbrowser.ui.search;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.utilities.Constants;

import java.util.ArrayList;
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
    private List<UserEntry> currentResults;
    private SearchModel currentSearchModel;

    public SearchPresenterImp(SearchContract.View view, MainStorage storage) {
        this.view = view;
        this.storage = storage;
        currentResults = new ArrayList<>();
    }

    @Override
    public void searchUser(String userName) {
        Log.d(LOGTAG, "searchUser = " + userName);
        initializeSearchModel(SearchType.USER, userName);
        view.showLoading();
        currentResults = new ArrayList<>();
        storage.queryUsers(this, currentSearchModel);
    }

    @Override
    public void searchContributors(String repoName) {
        Log.d(LOGTAG, "searchContributors = " + repoName);
        initializeSearchModel(SearchType.CONTRIBUTORS, repoName);
        view.showLoading();
        storage.queryUsers(this, currentSearchModel);
    }

    @Override
    public void searchFollowers(String userName) {
        Log.d(LOGTAG, "searchFollowers = " + userName);
        initializeSearchModel(SearchType.FOLLOWERS, userName);
        view.showLoading();
        storage.queryUsers(this, currentSearchModel);
    }

    @Override
    public void searchFollowing(String userName) {
        Log.d(LOGTAG, "searchFollowing = " + userName);
        initializeSearchModel(SearchType.FOLLOWING, userName);
        view.showLoading();
        storage.queryUsers(this, currentSearchModel);
    }

    @Override
    public void onNoResultsFound() {
        view.showViews();
        view.showNoResultsView();
    }

    @Override
    public void onSearchSuccess(List<UserEntry> userList) {
        Log.d(LOGTAG, "onSearchSuccess");
        currentResults.addAll(userList);
        currentSearchModel.incrementResultsCount(userList.size());
        hasMoreResults = true;
        if (currentResults.size() % Constants.SEARCH_QUERIES_MAX_PER_PAGE != 0) {
            hasMoreResults = false;
        }
        view.setSearchResult(currentResults);
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
            storage.loadMoreSearchResults(this, currentSearchModel);
        }
    }

    private void initializeSearchModel(SearchType type, String searchCriteria) {
        currentSearchModel = new SearchModel(type, searchCriteria);
    }
}
