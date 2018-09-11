package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.BranchEntry;
import com.antondevs.apps.githubbrowser.data.database.model.CommitEntry;
import com.antondevs.apps.githubbrowser.data.database.model.ReleaseEntry;
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

    public static Observable<Integer> getRepoContributorsCount(final RemoteAPIService service, final String repoFullName) {
        HashMap<String, String> queryMap = new HashMap<>();

        return service.queryRepoContributors(repoFullName, queryMap)
                .flatMap(new Function<Response<List<UserEntry>>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Response<List<UserEntry>> listResponse) throws Exception {
                        HashMap<String, String> lastPageQuery = new HashMap<>();
                        if (listResponse.headers().get("Link") != null) {
                            lastPageQuery.put("page", RepoBuilder.getLastPageFromLinkHeader(listResponse.headers().get("Link")));
                        }
                        return Observable.just(listResponse)
                                .zipWith(service.queryRepoContributors(repoFullName, lastPageQuery),
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

    public static Observable<Integer> getRepoCommitsCount(final RemoteAPIService service, final String repoFullName) {
        HashMap<String, String> queryMap = new HashMap<>();

        return service.queryRepoCommits(repoFullName, queryMap)
                .flatMap(new Function<Response<List<CommitEntry>>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Response<List<CommitEntry>> listResponse) throws Exception {
                        HashMap<String, String> lastPageQuery = new HashMap<>();
                        if (listResponse.headers().get("Link") != null) {
                            lastPageQuery.put("page", RepoBuilder.getLastPageFromLinkHeader(listResponse.headers().get("Link")));
                        }
                        return Observable.just(listResponse)
                                .zipWith(service.queryRepoCommits(repoFullName, lastPageQuery),
                                        new BiFunction<Response<List<CommitEntry>>, Response<List<CommitEntry>>, Integer>() {
                                            @Override
                                            public Integer apply(Response<List<CommitEntry>> listResponse,
                                                                 Response<List<CommitEntry>> listResponse2) throws Exception {
                                                List<CommitEntry> fromFirstResponse = listResponse.body();
                                                List<CommitEntry> fromSecondResponse = listResponse2.body();
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

    public static Observable<Integer> getRepoReleasesCount(final RemoteAPIService service, final String repoFullName) {
        HashMap<String, String> queryMap = new HashMap<>();

        return service.queryRepoReleases(repoFullName, queryMap)
                .flatMap(new Function<Response<List<ReleaseEntry>>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Response<List<ReleaseEntry>> listResponse) throws Exception {
                        HashMap<String, String> lastPageQuery = new HashMap<>();
                        if (listResponse.headers().get("Link") != null) {
                            lastPageQuery.put("page", RepoBuilder.getLastPageFromLinkHeader(listResponse.headers().get("Link")));
                        }
                        return Observable.just(listResponse)
                                .zipWith(service.queryRepoReleases(repoFullName, lastPageQuery),
                                        new BiFunction<Response<List<ReleaseEntry>>, Response<List<ReleaseEntry>>, Integer>() {
                                            @Override
                                            public Integer apply(Response<List<ReleaseEntry>> listResponse,
                                                                 Response<List<ReleaseEntry>> listResponse2) throws Exception {
                                                List<ReleaseEntry> fromFirstResponse = listResponse.body();
                                                List<ReleaseEntry> fromSecondResponse = listResponse2.body();
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
                                        }).onErrorReturn(new Function<Throwable, Integer>() {
                                    @Override
                                    public Integer apply(Throwable throwable) throws Exception {
                                        Log.d(LOGTAG, "getRepoReleasesCount.Observable.just.onErrorReturn");
                                        return 0;
                                    }
                                });
                    }
                });

    }

    public static Observable<Integer> getRepoBranchesCount(final RemoteAPIService service, final String repoFullName) {
        HashMap<String, String> queryMap = new HashMap<>();

        return service.queryRepoBranches(repoFullName, queryMap)
                .flatMap(new Function<Response<List<BranchEntry>>, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Response<List<BranchEntry>> listResponse) throws Exception {
                        HashMap<String, String> lastPageQuery = new HashMap<>();
                        if (listResponse.headers().get("Link") != null) {
                            lastPageQuery.put("page", RepoBuilder.getLastPageFromLinkHeader(listResponse.headers().get("Link")));
                        }
                        return Observable.just(listResponse)
                                .zipWith(service.queryRepoBranches(repoFullName, lastPageQuery),
                                        new BiFunction<Response<List<BranchEntry>>, Response<List<BranchEntry>>, Integer>() {
                                            @Override
                                            public Integer apply(Response<List<BranchEntry>> listResponse,
                                                                 Response<List<BranchEntry>> listResponse2) throws Exception {
                                                List<BranchEntry> fromFirstResponse = listResponse.body();
                                                List<BranchEntry> fromSecondResponse = listResponse2.body();
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

}
