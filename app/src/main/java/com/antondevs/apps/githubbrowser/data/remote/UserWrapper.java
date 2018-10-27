package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Anton.
 */
public interface UserWrapper {

    Maybe<UserEntry> createUser();

    Single<UserEntry> loadMoreOwnedRepos();

    Single<UserEntry> loadMoreStarredRepos();
}
