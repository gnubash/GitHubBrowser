package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Anton.
 */
public class RemoteOperationsTest implements MainStorage {

    private static final String LOGTAG = RemoteOperationsTest.class.getSimpleName();

    private RemoteAPIService apiService;

    private DatabaseHelper databaseHelper;

    private String basicCredentials;

    public RemoteOperationsTest(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void checkCredentials(AuthenticationListener listener) {

    }

    @Override
    public void performAuthentication(final String username, final String password, final AuthenticationListener listener) {
        basicCredentials = Credentials.basic(username, password, UTF_8);
        if (apiService == null) {
            apiService = APIService.getService();
        }
        apiService.loginUser(basicCredentials).enqueue(new Callback<UserEntry>() {
            @Override
            public void onResponse(Call<UserEntry> call, Response<UserEntry> response) {
                if (response.code() == 401) {
                    listener.onAuthenticationFailed();
                    return;
                }
                Log.d(LOGTAG, "Request success onResponse()");
                UserEntry user = response.body();
                Log.d(LOGTAG, user.toString());
                AuthEntry authEntry = new AuthEntry(username, password);

                Log.d(LOGTAG, authEntry.toString());
                listener.onUserAuthenticated();
            }

            @Override
            public void onFailure(Call<UserEntry> call, Throwable t) {
                Log.d(LOGTAG, "Request success onFailure()");
                listener.onAuthenticationFailed();
            }
        });
    }

    @Override
    public void queryUser(UserListener listener, String loginName) {

    }

    @Override
    public void queryUsers(SearchListener listener, String loginName) {

    }

    @Override
    public void queryRepo(RepoListener listener, String repoName) {

    }

    @Override
    public void queryFollowers(SearchListener listener, String loginName) {

    }

    @Override
    public void queryFollowing(SearchListener listener, String loginName) {

    }

    @Override
    public void queryContributors(SearchListener listener, String repoName) {

    }
}
