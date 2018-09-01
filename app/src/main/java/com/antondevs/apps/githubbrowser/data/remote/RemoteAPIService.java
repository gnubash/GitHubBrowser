package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Anton.
 */
public interface RemoteAPIService {

    // User
    @GET("users/{login_name}")
    Call<UserEntry> queryUser(@Header("Authorization") String authKey,
                              @Path("login_name") String loginName);

    // Repo
    @GET("repos/{full_name}")
    Call<RepoEntry> queryRepo(@Header("Authorization") String authKey,
                                                @Path(value = "full_name", encoded = true) String full_name);

    // User owned
    @GET("users/{login_name}/repos")
    Call <List<RepoEntry>> queryUserOwnedRepos(@Header("Authorization") String authKey,
                                               @Path("login_name") String loginName);

    //User starred
    @GET("users/{login_name}/starred")
    Call<List<RepoEntry>> queryUserStarredRepos(@Header("Authorization") String authKey,
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

    // Repo Contributors
    @GET("repos/{full_name}/contributors")
    Call<List<UserEntry>> queryRepoContributors(@Header("Authorization") String authKey,
                                                @Path("full_name") String full_name);
}
