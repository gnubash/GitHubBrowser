package com.antondevs.apps.githubbrowser.data.remote;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.utilities.Utils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by Anton.
 */
public class RepoResponsePaging implements ResponsePaging<List<RepoEntry>> {

    private static final String LOGTAG = RepoResponsePaging.class.getSimpleName();

    private Map<String, String> currentQueryMap;
    private String pagesCount;
    private String nextPage;
    private String searchUrl;
    private RemoteAPIService service;

    public RepoResponsePaging() {
        Log.d(LOGTAG, "RepoResponsePaging");

        service = APIService.getService();
    }

    @Override
    public Single<List<RepoEntry>> search(String url, Map<String, String> queryMap) {

        Log.d(LOGTAG, "searchUsers " + url + " " + queryMap.toString());

        searchUrl = url;
        currentQueryMap = queryMap;
        nextPage = "";
        pagesCount = "";
        return service.queryUserRepos(searchUrl, currentQueryMap)
                .flatMap(new Function<Response<List<RepoEntry>>, SingleSource<? extends List<RepoEntry>>>() {
            @Override
            public SingleSource<? extends List<RepoEntry>> apply(Response<List<RepoEntry>> listResponse) throws Exception {

                Log.d(LOGTAG, "queryUserRepos.apply " + listResponse.toString());

                if (listResponse.headers().get("Link") != null) {
                    String linkHeader = listResponse.headers().get("Link");
                    nextPage = Utils.getNextPageFromLinkHeader(linkHeader);
                    currentQueryMap.put("page", nextPage);
                    Log.d(LOGTAG, "search.apply nextPage = " + nextPage +
                            " currentQueryMap = " + currentQueryMap);
                }

                return Single.just(listResponse.body());
            }
        });

    }

    @Override
    public Single<List<RepoEntry>> getNextPage() {

        if (hasMorePages()) {
            return service.queryUserRepos(searchUrl, currentQueryMap)
                    .flatMap(new Function<Response<List<RepoEntry>>, SingleSource<? extends List<RepoEntry>>>() {
                        @Override
                        public SingleSource<? extends List<RepoEntry>> apply(Response<List<RepoEntry>> listResponse)
                                throws Exception {

                            Log.d(LOGTAG, "getNextPageRepos.apply");

                            nextPage = Utils.getNextPageFromLinkHeader(listResponse.headers().get("Link"));
                            currentQueryMap.put("page", nextPage);
                            Log.d(LOGTAG, "getNextPageRepos.apply nextPage = " + nextPage +
                                    " currentQueryMap = " + currentQueryMap);
                            return Single.just(listResponse.body());
                        }
                    });
        }
        else {
            throw new NoSuchElementException("No more pages");
        }

    }

    @Override
    public boolean hasMorePages() {
        return (nextPage != null && !nextPage.isEmpty());
    }

}
