package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;
import com.antondevs.apps.githubbrowser.data.remote.ResponsePaging;
import com.antondevs.apps.githubbrowser.utilities.Methods;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
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
    public Observable<List<RepoEntry>> search(String url, Map<String, String> queryMap) {

        Log.d(LOGTAG, "searchUsers " + searchUrl + queryMap.toString());

        searchUrl = url;
        currentQueryMap = queryMap;
        nextPage = "";
        pagesCount = "";
        return service.queryUserRepos(searchUrl, currentQueryMap)
                .flatMap(new Function<Response<List<RepoEntry>>, ObservableSource<? extends List<RepoEntry>>>() {
            @Override
            public ObservableSource<? extends List<RepoEntry>> apply(Response<List<RepoEntry>> listResponse) throws Exception {

                Log.d(LOGTAG, "queryUserRepos.apply " + listResponse.toString());

                if (listResponse.headers().get("Link") != null) {
                    String linkHeader = listResponse.headers().get("Link");
                    nextPage = Methods.getNextPageFromLinkHeader(linkHeader);
                    currentQueryMap.put("page", nextPage);
                    pagesCount = Methods.getLastPageFromLinkHeader(linkHeader);
                }

                return Observable.just(listResponse.body());
            }
        });
    }

    @Override
    public Observable<List<RepoEntry>> getNextPage() {

        if (hasMorePages()) {
            return service.queryUserRepos(searchUrl, currentQueryMap)
                    .flatMap(new Function<Response<List<RepoEntry>>, ObservableSource<? extends List<RepoEntry>>>() {
                        @Override
                        public ObservableSource<? extends List<RepoEntry>> apply(Response<List<RepoEntry>> listResponse)
                                throws Exception {

                            Log.d(LOGTAG, "getNextPageRepos.apply");

                            nextPage = Methods.getNextPageFromLinkHeader(listResponse.headers().get("Link"));
                            currentQueryMap.put("page", nextPage);
                            return Observable.just(listResponse.body());
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
