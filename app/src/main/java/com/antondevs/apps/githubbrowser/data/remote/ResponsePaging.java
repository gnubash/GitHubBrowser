package com.antondevs.apps.githubbrowser.data.remote;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Anton.
 */
public interface ResponsePaging<T> {

    Observable<T> search(String url, Map<String, String> queryMap);

    Observable<T> getNextPage();

    boolean hasMorePages();
}
