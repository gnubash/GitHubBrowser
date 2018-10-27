package com.antondevs.apps.githubbrowser.data.remote;

import java.util.Map;

import io.reactivex.Single;

/**
 * Created by Anton.
 */
public interface ResponsePaging<T> {

    Single<T> search(String url, Map<String, String> queryMap);

    Single<T> getNextPage();

    boolean hasMorePages();
}
