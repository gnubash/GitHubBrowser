package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * Created by Anton.
 */
public class RepoBuilder {

    private static final String LOGTAG = RepoBuilder.class.getSimpleName();

    public static Observable<Integer> getRepoContributorsCount(final String authentication,
                                                               final RemoteAPIService service, final String repoFullName) {
        HashMap<String, String> queryMap = new HashMap<>();

        return service.queryRepoContributors(authentication, repoFullName, queryMap)
                .flatMap(new Function<Response<List<UserEntry>>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Response<List<UserEntry>> listResponse) throws Exception {
                        HashMap<String, String> lastPageQuery = new HashMap<>();
                        if (listResponse.headers().get("Link") != null) {
                            lastPageQuery.put("page", RepoBuilder.getLastPageFromLinkHeader(listResponse.headers().get("Link")));
                        }
                        return Observable.just(listResponse)
                                .zipWith(service.queryRepoContributors(authentication, repoFullName, lastPageQuery),
                                        new BiFunction<Response<List<UserEntry>>, Response<List<UserEntry>>, Integer>() {
                                            @Override
                                            public Integer apply(Response<List<UserEntry>> listResponse,
                                                                 Response<List<UserEntry>> listResponse2) throws Exception {
                                                List<UserEntry> fromFirstResponse = listResponse.body();
                                                List<UserEntry> fromSecondResponse = listResponse2.body();
                                                if (listResponse.headers().get("Link") != null) {
                                                    String pages = RepoBuilder.getLastPageFromLinkHeader(listResponse.headers().get("Link"));
                                                    Integer pagesCount = Integer.valueOf(pages) - 1;
                                                    Log.d(LOGTAG, "pagesCount after " + pagesCount);
                                                    Log.d(LOGTAG, "Calculating " + pagesCount + " * " +
                                                            fromFirstResponse.size() + " + " + fromSecondResponse.size());
                                                    Integer totalCount = (pagesCount * fromFirstResponse.size()) + fromSecondResponse.size();
                                                    Log.d(LOGTAG, "totalCount = " + totalCount);
                                                    return totalCount;
                                                }
                                                return fromFirstResponse.size();
                                            }
                                        });
                    }
                });

    }


    private static String getLastPageFromLinkHeader(String linkHeader) {

        int startIndexForSearch = linkHeader.indexOf("next");

        String searchCriteria = "page=";

        int start = linkHeader.indexOf(searchCriteria, startIndexForSearch);
        int end = linkHeader.indexOf('>', startIndexForSearch);
        String substring = linkHeader.substring(start + searchCriteria.length(), end);
        Log.d(LOGTAG, "getLastPageFromLinkHeader() substring = " + substring);
        return substring;
    }

    private static int getPrevPageFromLinkHeader(String linkHeader) {

        int startIndexForSearch = linkHeader.indexOf("prev");

        String searchCriteria = "page=";

        String substring = linkHeader.substring(linkHeader.indexOf(searchCriteria) +
                searchCriteria.length(), linkHeader.indexOf('&'));

        return Integer.valueOf(substring);
    }
}
