package com.antondevs.apps.githubbrowser.data.cache;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Anton.
 */
public class CacheStorage implements LocalCache{

    private static final String LOGTAG = CacheStorage.class.getSimpleName();

    private long observableDelay = 100L;
    private TimeUnit timeUnitMillis = TimeUnit.MILLISECONDS;

    private Map<String, UserEntry> userCache;
    private Map<String, RepoEntry> repoCache;

    private CacheStorage() {
        Log.d(LOGTAG, "CacheStorage");
        userCache = new HashMap<>();
        repoCache = new HashMap<>();
    }

    public static LocalCache getInstance() {
        Log.d(LOGTAG, "CacheStorage.getInstance");
        return new CacheStorage();
    }

    @Override
    public Maybe<UserEntry> getUser(String loginName) {
        Log.d(LOGTAG, "CacheStorage.getUser");
        return (userCache.containsKey(loginName))
                ? Maybe.just(userCache.get(loginName)).delay(observableDelay, timeUnitMillis) : Maybe.<UserEntry>empty();
    }

    @Override
    public Maybe<RepoEntry> getRepo(String repoFullName) {
        Log.d(LOGTAG, "CacheStorage.getRepo");
        return (repoCache.containsKey(repoFullName))
                ? Maybe.just(repoCache.get(repoFullName)).delay(observableDelay, timeUnitMillis) : Maybe.<RepoEntry>empty();
    }

    @Override
    public void addUser(UserEntry userEntry) {
        Log.d(LOGTAG, "CacheStorage.addUser");
        userCache.put(userEntry.getLogin(), userEntry);
    }

    @Override
    public void addRepo(RepoEntry repoEntry) {
        Log.d(LOGTAG, "CacheStorage.addRepo");
        repoCache.put(repoEntry.getFull_name(), repoEntry);
    }

    @Override
    public String getRepoContributorsUrl(String repoName) {
        return repoCache.get(repoName).getContributors_url();
    }

    @Override
    public String getUserFollowersUrl(String loginName) {
        return userCache.get(loginName).getFollowers_url();
    }

    @Override
    public String getUserFollowingUrl(String loginName) {
        return userCache.get(loginName).getFollowing_url();
    }
}
