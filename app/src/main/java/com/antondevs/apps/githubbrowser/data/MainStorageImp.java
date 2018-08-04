package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Anton.
 */
public class MainStorageImp implements MainStorage {

    private static final String LOGTAG = MainStorageImp.class.getSimpleName();

    private RemoteAPIService apiService;

    private DatabaseHelper databaseHelper;

    private String basicCredentials;

    public MainStorageImp(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void checkCredentials(AuthenticationListener listener) {
        if (databaseHelper.getStoredAuth() != 1) {
            Log.d(LOGTAG, "databaseHelper.getStoredAuth() = " + databaseHelper.getStoredAuth());
//            databaseHelper.clearStoredAuth();
            listener.onAuthenticationRequered();
        }
        else {
            listener.onUserAuthenticated();
        }
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

                databaseHelper.writeAuthnetication(authEntry);
                databaseHelper.writeUser(user);

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
