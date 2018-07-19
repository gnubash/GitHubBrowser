package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.data.database.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Anton.
 */
public interface RemoteAPIService {

    @GET("users/{login_name}")
    Call<UserEntry> queryUser(@Path("login_name") String loginName);

    @GET("users/{login_name}/repos")
    Call <List<RepoEntry>> queryUserOwnedRepos(@Path("login_name") String loginName);

    //Starred
    @GET("users/{login_name}/starred")
    Call<List<RepoEntry>> queryUserStarredRepos(@Path("login_name") String loginName);

    // Followers
    @GET("users/{login_name}/followers")
    Call<List<UserEntry>> queryUserFollowers(@Path("login_name") String login_name);

    // Following
    @GET("users/{login_name}/following")
    Call<List<UserEntry>> queryUserFollowing(@Path("login_name") String login_name);

    // Search
    @GET("search/users")
    Call<List<UserEntry>> searchUsers(@Query("login_name") String login_name);

    // Repo Contributors
    @GET("repos/{login_name}/{repo_name}/contributors")
    Call<List<UserEntry>> queryRepoContributors(@Path("login_name") String login_name,
                                                @Path("repo_name") String repo_name);
}
