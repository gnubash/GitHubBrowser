package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.data.database.model.BranchEntry;
import com.antondevs.apps.githubbrowser.data.database.model.CommitEntry;
import com.antondevs.apps.githubbrowser.data.database.model.ReleaseEntry;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Anton.
 */
public interface RemoteAPIService {

    // User
    @GET("users/{login_name}")
    Maybe<UserEntry> queryUser(@Path("login_name") String loginName);

    // Repo
    @GET("repos/{full_name}")
    Single<RepoEntry> queryRepo(@Path(value = "full_name", encoded = true) String full_name);

    // User owned and starred repos
    @GET
    Single<Response<List<RepoEntry>>> queryUserRepos(@Url String url,
                                                         @QueryMap(encoded = true) Map<String, String> queryMap);

    // Dynamic Search - users, repo contributors, followers and following
    @GET
    Single<Response<okhttp3.ResponseBody>> searchUsers(@Url String url,
                                                           @QueryMap(encoded = true) Map<String, String> queryMap);

    // Images
    @GET
    Single<okhttp3.ResponseBody> getImage(@Url String url);

    // Check if repository by logged user
    @GET("/user/starred/{full_repo_name}")
    Completable checkIfRepoIsStarred(@Path(value = "full_repo_name", encoded = true) String full_repo_name);

    // Star repository
    @Headers("Content-Length: 0")
    @PUT("/user/starred/{full_repo_name}")
    Completable starRepo(@Path(value = "full_repo_name", encoded = true) String full_repo_name);

    // Unstar repo
    @Headers("Content-Length: 0")
    @DELETE("/user/starred/{full_repo_name}")
    Completable unstarRepo(@Path(value = "full_repo_name", encoded = true) String full_repo_name);


    // Utils required to form full RepoEntry

    // Repo Commits
    @GET("repos/{full_name}/commits")
    Single<Response<List<CommitEntry>>> queryRepoCommits(@Path(value = "full_name", encoded = true) String full_name,
                                                             @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Branches
    @GET("repos/{full_name}/branches")
    Single<Response<List<BranchEntry>>> queryRepoBranches(@Path(value = "full_name", encoded = true) String full_name,
                                                              @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Releases
    @GET("repos/{full_name}/git/refs/tags")
    Single<Response<List<ReleaseEntry>>> queryRepoReleases(@Path(value = "full_name", encoded = true) String full_name,
                                                               @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Contributors
    @GET("repos/{full_name}/contributors")
    Single<Response<List<UserEntry>>> queryRepoContributors(@Path(value = "full_name", encoded = true) String full_name,
                                                                @QueryMap(encoded = true) Map<String, String> queryMap);

}
