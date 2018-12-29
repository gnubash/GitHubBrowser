package com.antondevs.apps.githubbrowser.ui.search;

import android.net.Uri;
import android.util.Log;

import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.ui.AbsGitHubPresenter;
import com.antondevs.apps.githubbrowser.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Anton.
 */
public class SearchPresenterImp extends AbsGitHubPresenter implements SearchContract.Presenter{

    private static final String LOGTAG = SearchPresenterImp.class.getSimpleName();

    private SearchContract.View view;
    private boolean hasMoreResults;
    private List<UserEntry> currentResults;
    private SearchModel currentSearchModel;
    private boolean isLoading;
    private boolean userNotified;

    public SearchPresenterImp(SearchContract.View view, MainStorage storage) {
        super(view, storage);
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
        searchUser();
    }

    @Override
    public void searchContributors(String repoName) {
        Log.d(LOGTAG, "searchContributors = " + repoName);
        initializeSearchModel(SearchType.CONTRIBUTORS, repoName);
        view.showLoading();
        searchUser();
    }

    @Override
    public void searchFollowers(String userName) {
        Log.d(LOGTAG, "searchFollowers = " + userName);
        initializeSearchModel(SearchType.FOLLOWERS, userName);
        view.showLoading();
        searchUser();
    }

    @Override
    public void searchFollowing(String userName) {
        Log.d(LOGTAG, "searchFollowing = " + userName);
        initializeSearchModel(SearchType.FOLLOWING, userName);
        view.showLoading();
        searchUser();
    }

    @Override
    public void userScrollToBottom() {
        Log.d(LOGTAG, "userScrollToBottom");
        if (hasMoreResults && !isLoading) {
            view.showLoadingMoreResults();
            storage.loadMoreSearchResults(currentSearchModel)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<UserEntry>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.d(LOGTAG, "userScrollToBottom.doOnSubscribe");
                            isLoading = true;
                        }

                        @Override
                        public void onSuccess(List<UserEntry> userEntries) {
                            Log.d(LOGTAG, "userScrollToBottom.onSuccess");
                            currentResults.addAll(userEntries);
                            currentSearchModel.incrementResultsCount(userEntries.size());
                            hasMoreResults = true;
                            if (currentResults.size() % Constants.SEARCH_QUERIES_MAX_PER_PAGE != 0) {
                                hasMoreResults = false;
                            }
                            view.resultsLoaded();
                            view.showViews();
                            isLoading = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(LOGTAG, "userScrollToBottom.onError");
                            e.printStackTrace();
                            hasMoreResults = false;
                            isLoading = false;
                            view.showNoMoreSearchResults();
                        }
                    });
        }
        if (!hasMoreResults && !userNotified) {
            userNotified = true;
            view.showNoMoreSearchResults();
        }
    }

    private void initializeSearchModel(SearchType type, String searchCriteria) {
        Log.d(LOGTAG, "initializeSearchModel type = " + type + " criteria = " + searchCriteria);
        currentSearchModel = new SearchModel(type, searchCriteria);
    }

    private void searchUser() {
        storage.queryUsers(currentSearchModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<UserEntry>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(LOGTAG, "searchUser.onSubscribe");
                    }

                    @Override
                    public void onSuccess(List<UserEntry> userEntries) {
                        Log.d(LOGTAG, "searchUser.onSuccess userEntries " + userEntries.toString());
                        if (userEntries.size() == 0) {
                            view.showNoResultsView();
                            return;
                        }
                        currentResults.addAll(userEntries);
                        currentSearchModel.incrementResultsCount(userEntries.size());
                        hasMoreResults = true;
                        if (currentResults.size() % Constants.SEARCH_QUERIES_MAX_PER_PAGE != 0) {
                            hasMoreResults = false;
                        }
                        view.resultsLoaded();
                        view.showViews();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(LOGTAG, "searchUser.onError");
                        hasMoreResults = false;
                        view.showNoResultsView();
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void bindViewToPosition(int position, SearchContract.ViewSearchResultItem viewSearchResultItem) {
        Log.d(LOGTAG, "bindViewToPosition");
        viewSearchResultItem.setLoginName(currentResults.get(position).getLogin());
        viewSearchResultItem.setImageUri(Uri.parse(currentResults.get(position).getAvatar_url()));
    }

    @Override
    public int getItemsCount() {
        Log.d(LOGTAG, "getItemsCount");
        return currentResults.size();
    }
}
