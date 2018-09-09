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
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Anton.
 */
public interface RemoteAPIService {

    // User
    @GET("users/{login_name}")
    Single<UserEntry> queryUser(@Header("Authorization") String authKey,
                                @Path("login_name") String loginName);

    // Repo
    @GET("repos/{full_name}")
    Call<RepoEntry> queryRepo(@Header("Authorization") String authKey,
                                                @Path(value = "full_name", encoded = true) String full_name);

    // User owned
    @GET("users/{login_name}/repos")
    Single<List<RepoEntry>> queryUserOwnedRepos(@Header("Authorization") String authKey,
                                               @Path("login_name") String loginName);

    //User starred
    @GET("users/{login_name}/starred")
    Single<List<RepoEntry>> queryUserStarredRepos(@Header("Authorization") String authKey,
                                                @Path("login_name") String loginName);

    // Followers
    @GET("users/{login_name}/followers")
    Call<List<UserEntry>> queryUserFollowers(@Header("Authorization") String authKey,
                                             @Path("login_name") String login_name);

    // Following
    @GET("users/{login_name}/following")
    Call<List<UserEntry>> queryUserFollowing(@Header("Authorization") String authKey,
                                             @Path("login_name") String login_name);

    // Search
    @GET("search/users")
    Call<List<UserEntry>> searchUsers(@Header("Authorization") String authKey,
                                      @Query("login_name") String login_name);

    // Methods required to form full RepoEntry

    // Repo Commits
    @GET("repos/{full_name}/commits")
    Observable<Response<List<CommitEntry>>> queryRepoCommits(@Header("Authorization") String authKey,
                                                             @Path(value = "full_name", encoded = true) String full_name,
                                                             @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Branches
    @GET("repos/{full_name}/branches")
    Observable<Response<List<BranchEntry>>> queryRepoBranches(@Header("Authorization") String authKey,
                                                              @Path(value = "full_name", encoded = true) String full_name,
                                                              @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Releases
    @GET("repos/{full_name}/git/refs/tags")
    Observable<Response<List<ReleaseEntry>>> queryRepoReleases(@Header("Authorization") String authKey,
                                                               @Path(value = "full_name", encoded = true) String full_name,
                                                               @QueryMap(encoded = true) Map<String, String> queryMap);

    // Repo Contributors
    @GET("repos/{full_name}/contributors")
    Observable<Response<List<UserEntry>>> queryRepoContributors(@Header("Authorization") String authKey,
                                                                 @Path(value = "full_name", encoded = true) String full_name,
                                                                 @QueryMap(encoded = true) Map<String, String> queryMap);

}
