package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Anton.
 */
public interface UserWrapper {

    Observable<UserEntry> createUser();

    Observable<List<RepoEntry>> loadMoreOwnedRepos();

    Observable<List<RepoEntry>> loadMoreStarredRepos();
}
