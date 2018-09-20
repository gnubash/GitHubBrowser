package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.data.database.model.BranchEntry;
import com.antondevs.apps.githubbrowser.data.database.model.CommitEntry;
import com.antondevs.apps.githubbrowser.data.database.model.ReleaseEntry;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Anton.
 */
public interface RemoteAPIService {

    // User
    @GET("users/{login_name}")
    Single<UserEntry> queryUser(@Path("login_name") String loginName);

    // Repo
    @GET("repos/{full_name}")
    Call<RepoEntry> queryRepo(@Path(value = "full_name", encoded = true) String full_name);

    // User owned
    @GET("users/{login_name}/repos")
    Single<List<RepoEntry>> queryUserOwnedRepos(@Path("login_name") String loginName);

    //User starred
    @GET("users/{login_name}/starred")
    Single<List<RepoEntry>> queryUserStarredRepos(@Path("login_name") String loginName);

    // Dynamic Search - users, repo contributors, followers and following
    @GET
    Observable<Response<okhttp3.ResponseBody>> searchUsers(@Url String url,
                                                           @QueryMap(encoded = true) Map<String, String> queryMap);

    // Methods required to form full RepoEntry

    // Repo Commits
    @GET("repos/{full_name}/commits")
    Observable<Response<List<CommitEntry>>> queryRepoCommits(@Path(value = "full_name", encoded = true) String full_name,
                                                             @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Branches
    @GET("repos/{full_name}/branches")
    Observable<Response<List<BranchEntry>>> queryRepoBranches(@Path(value = "full_name", encoded = true) String full_name,
                                                              @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Releases
    @GET("repos/{full_name}/git/refs/tags")
    Observable<Response<List<ReleaseEntry>>> queryRepoReleases(@Path(value = "full_name", encoded = true) String full_name,
                                                               @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Contributors
    @GET("repos/{full_name}/contributors")
    Observable<Response<List<UserEntry>>> queryRepoContributors(@Path(value = "full_name", encoded = true) String full_name,
                                                                 @QueryMap(encoded = true) Map<String, String> queryMap);

}
