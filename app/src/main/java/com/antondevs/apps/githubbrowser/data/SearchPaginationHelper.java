package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.GsonSearchResponseAdapter;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;
import com.antondevs.apps.githubbrowser.data.remote.ResponsePaginator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Anton.
 */
public class SearchPaginationHelper implements ResponsePaginator<List<UserEntry>> {

    private static final String LOGTAG = SearchPaginationHelper.class.getSimpleName();

    private Map<String, String> currentQueryMap;
    private String pagesCount;
    private String nextPage;
    private String searchUrl;
    private RemoteAPIService service;

    public SearchPaginationHelper() {

        Log.d(LOGTAG, "SearchPaginationHelper");

        service = APIService.getService();

    }

    @Override
    public Observable<List<UserEntry>> search(String searchUrl, Map<String, String> queryMap) {

        Log.d(LOGTAG, "searchUsers " + searchUrl + queryMap.toString());

        this.searchUrl = searchUrl;
        currentQueryMap = queryMap;
        nextPage = "";
        pagesCount = "";

        return service.searchUsers(searchUrl, queryMap)
                .flatMap(new Function<Response<ResponseBody>, ObservableSource<? extends List<UserEntry>>>() {
            @Override
            public ObservableSource<? extends List<UserEntry>> apply(Response<ResponseBody> responseBodyResponse)
                    throws Exception {

                Log.d(LOGTAG, "searchUsers.apply " + responseBodyResponse.toString());

                Type typeToken = new TypeToken<List<UserEntry>>() {}.getType();
                Gson gson = new GsonBuilder().
                        registerTypeAdapter(typeToken, new GsonSearchResponseAdapter()).create();
                ArrayList<UserEntry> users = gson.fromJson(responseBodyResponse.body().string(), typeToken);


                if (responseBodyResponse.headers().get("Link") != null) {
                    String linkHeader = responseBodyResponse.headers().get("Link");
                    nextPage = getNextPageFromLinkHeader(linkHeader);
                    currentQueryMap.put("page=", nextPage);
                    pagesCount = getLastPageFromLinkHeader(linkHeader);
                }

                return Observable.just(users);
            }
        });
    }

    @Override
    public Observable<List<UserEntry>> getNextPage() {

        if (hasMorePages()) {
            return service.searchUsers(searchUrl, currentQueryMap)
                    .concatMap(new Function<Response<ResponseBody>, ObservableSource<? extends List<UserEntry>>>() {
                @Override
                public ObservableSource<? extends List<UserEntry>> apply(Response<ResponseBody> responseBodyResponse)
                        throws Exception {

                    Log.d(LOGTAG, "getNextPageSearchResults.apply " + responseBodyResponse.toString());

                    Type typeToken = new TypeToken<List<UserEntry>>() {}.getType();
                    Gson gson = new GsonBuilder().
                            registerTypeAdapter(typeToken, new GsonSearchResponseAdapter()).create();
                    nextPage = getNextPageFromLinkHeader(responseBodyResponse.headers().get("Link"));
                    List<UserEntry> users = gson.fromJson(responseBodyResponse.body().string(), typeToken);

                    return Observable.just(users);
                }
            });
        }
        else {
            throw new NoSuchElementException("No more pages.");
        }

    }

    @Override
    public boolean hasMorePages() {
        return (nextPage != null && !nextPage.isEmpty());
    }

    private String getLastPageFromLinkHeader(String linkHeader) {

        int indexOfLastRel = linkHeader.indexOf(">; rel=\"last\"");

        String searchCriteria = "page=";

        int indexOfLastPage = linkHeader.lastIndexOf(searchCriteria);

        if (indexOfLastRel == -1 || indexOfLastPage == -1) {
            return "";
        }

        String substring = linkHeader.substring(indexOfLastPage + searchCriteria.length(), indexOfLastRel);
        Log.d(LOGTAG, "getLastPageFromLinkHeader() substring = " + substring);
        return substring;
    }

    private String getNextPageFromLinkHeader(String linkHeader) {

        int indexOfLastRel = linkHeader.indexOf(">; rel=\"next\"");

        String searchCriteria = "page=";

        int indexOfLastPage = linkHeader.indexOf(searchCriteria);

        if (indexOfLastRel == -1 || indexOfLastPage == -1) {
            return "";
        }

        String substring = linkHeader.substring(indexOfLastPage + searchCriteria.length(), indexOfLastRel);
        Log.d(LOGTAG, "getLastPageFromLinkHeader() substring = " + substring);
        return substring;
    }
}
