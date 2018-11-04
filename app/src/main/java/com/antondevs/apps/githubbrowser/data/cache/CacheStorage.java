package com.antondevs.apps.githubbrowser.data.cache;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;

/**
 * Created by Anton.
 */
public class CacheStorage implements LocalCache{

    private static final String LOGTAG = CacheStorage.class.getSimpleName();

    private Cache<String, UserEntry> userCache;
    private Cache<String, RepoEntry> repoCache;

    private long cacheCapacity = 500L;
    private long expireTime = 10L;
    private TimeUnit expireTimeUnit = TimeUnit.MINUTES;

    private long observableDelay = 100L;
    private TimeUnit timeUnitMillis = TimeUnit.MILLISECONDS;

    private CacheStorage() {
        Log.d(LOGTAG, "CacheStorage");
        createCaches();
    }

    public static LocalCache getInstance() {
        Log.d(LOGTAG, "getInstance");
        return new CacheStorage();
    }

    @Override
    public Maybe<UserEntry> getUser(String loginName) {
        Log.d(LOGTAG, "getUser");
        UserEntry tempUser = userCache.getIfPresent(loginName);

        return (tempUser == null) ? Maybe.<UserEntry>empty() : Maybe.just(tempUser).delay(observableDelay, timeUnitMillis);

    }

    @Override
    public Maybe<RepoEntry> getRepo(String repoFullName) {
        Log.d(LOGTAG, "getRepo");
        RepoEntry tempRepo = repoCache.getIfPresent(repoFullName);
        return (tempRepo == null) ? Maybe.<RepoEntry>empty() : Maybe.just(tempRepo).delay(observableDelay, timeUnitMillis);

    }

    @Override
    public void addUser(UserEntry userEntry) {
        Log.d(LOGTAG, "addUser");
        userCache.put(userEntry.getLogin(), userEntry);
    }

    @Override
    public void addRepo(RepoEntry repoEntry) {
        Log.d(LOGTAG, "addRepo");
        repoCache.put(repoEntry.getFull_name(), repoEntry);
    }

    @Override
    public String getRepoContributorsUrl(String repoName) {
        RepoEntry tempRepo = repoCache.getIfPresent(repoName);
        Log.d(LOGTAG, "getRepoContributorsUrl tempRepo " + tempRepo);
        return (tempRepo == null) ? "" : tempRepo.getContributors_url();
    }

    @Override
    public String getUserFollowersUrl(String loginName) {
        UserEntry tempUser = userCache.getIfPresent(loginName);
        Log.d(LOGTAG, "getUserFollowersUrl tempUser " + tempUser);
        return (tempUser == null) ? "" : tempUser.getFollowers_url();
    }

    @Override
    public String getUserFollowingUrl(String loginName) {
        UserEntry tempUser = userCache.getIfPresent(loginName);
        Log.d(LOGTAG, "getUserFollowingUrl tempUser " + tempUser);
        return (tempUser == null) ? "" : tempUser.getFollowing_url();
    }

    private void createCaches() {
        userCache = CacheBuilder.newBuilder()
                .maximumSize(cacheCapacity)
                .expireAfterWrite(expireTime, expireTimeUnit)
                .build();

        repoCache = CacheBuilder.newBuilder()
                .maximumSize(cacheCapacity)
                .expireAfterWrite(expireTime, expireTimeUnit)
                .build();
    }
}
