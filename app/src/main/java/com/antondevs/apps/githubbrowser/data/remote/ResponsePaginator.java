package com.antondevs.apps.githubbrowser.data.remote;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Anton.
 */
public interface ResponsePaginator<T> {

    public Observable<T> search(String url, Map<String, String> queryMap);

    public Observable<T> getNextPage();

    public boolean hasMorePages();
}
