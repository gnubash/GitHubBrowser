package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.GsonSearchResponseAdapter;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;
import com.antondevs.apps.githubbrowser.data.remote.ResponsePaging;
import com.antondevs.apps.githubbrowser.utilities.Constants;
import com.antondevs.apps.githubbrowser.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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
public class UserSearchPagingHelper implements ResponsePaging<List<UserEntry>> {

    private static final String LOGTAG = UserSearchPagingHelper.class.getSimpleName();

    private RemoteAPIService service;
    private Map<String, String> emptyMap;
    private String nextPageRel;

    public UserSearchPagingHelper() {

        Log.d(LOGTAG, "UserSearchPagingHelper");

        service = APIService.getService();
        emptyMap = new HashMap<>();

    }

    @Override
    public Observable<List<UserEntry>> search(String searchUrl, Map<String, String> queryMap) {

        Log.d(LOGTAG, "searchUsers " + searchUrl + queryMap.toString());

        Map<String, String> newHashMap = new HashMap<>();
        if (queryMap.containsKey("page")) {
            newHashMap.put("page", queryMap.get("page"));
        }
        if (queryMap.containsKey("q")) {
            newHashMap.put("q", queryMap.get("q"));
        }
        newHashMap.put("per_page", String.valueOf(Constants.SEARCH_QUERIES_MAX_PER_PAGE));

        return service.searchUsers(searchUrl, newHashMap)
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
                    nextPageRel = Utils.getNextPageRel(linkHeader);
                }

                return Observable.just(users);
            }
        });
    }

    @Override
    public Observable<List<UserEntry>> getNextPage() {

        if (hasMorePages()) {
            return service.searchUsers(nextPageRel, emptyMap)
                    .flatMap(new Function<Response<ResponseBody>, ObservableSource<? extends List<UserEntry>>>() {
                @Override
                public ObservableSource<? extends List<UserEntry>> apply(Response<ResponseBody> responseBodyResponse)
                        throws Exception {

                    Log.d(LOGTAG, "getNextPageSearchResults.apply " + responseBodyResponse.toString());

                    Type typeToken = new TypeToken<List<UserEntry>>() {}.getType();
                    Gson gson = new GsonBuilder().
                            registerTypeAdapter(typeToken, new GsonSearchResponseAdapter()).create();
                    nextPageRel = Utils.getNextPageRel(responseBodyResponse.headers().get("Link"));
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
        return (nextPageRel != null && !nextPageRel.isEmpty());
    }

}
