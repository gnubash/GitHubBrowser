package com.antondevs.apps.githubbrowser.data;

import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.model.AuthEntry;
import com.antondevs.apps.githubbrowser.data.database.DatabaseHelper;
import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static final MainStorage mainStorageUniqueInstance = new MainStorageImp();

    private RemoteAPIService apiService;

    private DatabaseHelper databaseHelper;

    private String basicCredentials;

    private UserEntry currentUser;

    private MainStorageImp() {
        apiService = APIService.getService();
    }

//    public MainStorageImp(DatabaseHelper databaseHelper) {
//        this.databaseHelper = databaseHelper;
//        apiService = APIService.getService();
//    }

    public static MainStorage getInstance() {
        return mainStorageUniqueInstance;
    }

    @Override
    public String getLoggedUser() {
        return databaseHelper.getAuthentication().getLogin();
    }

    @Override
    public void setDatabaseHelper(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void checkCredentials(AuthenticationListener listener) {
        if (databaseHelper.getStoredAuth() != 1) {
            Log.d(LOGTAG, "databaseHelper.getStoredAuth() = " + databaseHelper.getStoredAuth());
            listener.onAuthenticationRequered();
        }
        else {
            AuthEntry entry = databaseHelper.getAuthentication();
            basicCredentials = Credentials.basic(entry.getLogin(), entry.getPass(), UTF_8);
            listener.onUserAuthenticated();
        }
    }

    @Override
    public void performAuthentication(final String username, final String password, final AuthenticationListener listener) {
        basicCredentials = Credentials.basic(username, password, UTF_8);
        
        apiService.queryUser(basicCredentials, username).enqueue(new Callback<UserEntry>() {
            @Override
            public void onResponse(Call<UserEntry> call, Response<UserEntry> response) {
                if (response.code() == 401) {
                    listener.onAuthenticationFailed();
                    return;
                }
                Log.d(LOGTAG, "Request performAuthentication().onResponse()");
                UserEntry user = response.body();
                Log.d(LOGTAG, user.toString());
                AuthEntry authEntry = new AuthEntry(username, password);

                Log.d(LOGTAG, authEntry.toString());

                databaseHelper.writeAuthnetication(authEntry);

                getUserOwned(listener, user);

            }

            @Override
            public void onFailure(Call<UserEntry> call, Throwable t) {
                if (t instanceof IOException) {
                    Log.d(LOGTAG, "Request performAuthentication().onFailure() 't instanceof IOException'");
                    listener.onNetworkConnectionFailure();
                    return;
                }
                Log.d(LOGTAG, "Request performAuthentication().onFailure()");
                listener.onAuthenticationFailed();
            }
        });
    }

    @Override
    public void queryUser(final UserListener listener, final String loginName) {
        if (currentUser != null && currentUser.getLogin().equals(loginName)) {
            listener.onUserLoaded(currentUser);
            return;
        }

        apiService.queryUser(basicCredentials, loginName).enqueue(new Callback<UserEntry>() {
            @Override
            public void onResponse(Call<UserEntry> call, Response<UserEntry> response) {
                if (response.code() == 401) {
                    listener.onLoadFailed();
                    return;

                }
                Log.d(LOGTAG, "Request queryUser().queryUser().onResponse()");
                UserEntry user = response.body();
                Log.d(LOGTAG, user.toString());

                getUserOwned(listener, user);


            }

            @Override
            public void onFailure(Call<UserEntry> call, Throwable t) {

                Log.d(LOGTAG, "Request queryUser().queryUser().onFailure()");
                listener.onLoadFailed();
            }

        });

    }

    @Override
    public void queryUsers(SearchListener listener, String loginName) {

    }

    @Override
    public void queryRepo(final RepoListener listener, String repoName) {
        apiService.queryRepo(basicCredentials, currentUser.getLogin(), repoName).enqueue(new Callback<RepoEntry>() {
            @Override
            public void onResponse(Call<RepoEntry> call, Response<RepoEntry> response) {
                Log.d(LOGTAG, "Request queryRepo().onResponse()");
                RepoEntry repoEntry = response.body();
                Log.d(LOGTAG, repoEntry.toString());
                listener.onRepoLoaded(repoEntry);
            }

            @Override
            public void onFailure(Call<RepoEntry> call, Throwable t) {
                listener.onLoadFailed();
            }
        });
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

    private void getUserOwned(final UserListener listener,final UserEntry userEntry) {
        String loginName = userEntry.getLogin();
        apiService.queryUserOwnedRepos(basicCredentials, loginName).enqueue(new Callback<List<RepoEntry>>() {
            @Override
            public void onResponse(Call<List<RepoEntry>> call, Response<List<RepoEntry>> response) {
                Log.d(LOGTAG, "Request getUserOwned().queryUserOwnedRepos().onResponse()");
                List<RepoEntry> listOfRepos = response.body();
                List<String> repoNames = new ArrayList<>();

                for (RepoEntry r : listOfRepos) {
                    repoNames.add(r.getName());
                }
                userEntry.setOwnedRepos(repoNames);
                getUserStarred(listener, userEntry);

            }

            @Override
            public void onFailure(Call<List<RepoEntry>> call, Throwable t) {

            }
        });
    }

    private void getUserStarred(final UserListener listener, final UserEntry userEntry) {
        String loginName = userEntry.getLogin();
        apiService.queryUserStarredRepos(basicCredentials, loginName).enqueue(new Callback<List<RepoEntry>>() {
            @Override
            public void onResponse(Call<List<RepoEntry>> call, Response<List<RepoEntry>> response) {
                Log.d(LOGTAG, "Request getUserStarred().queryUserStarredRepos().onResponse()");
                List<RepoEntry> listOfRepos = response.body();
                List<String> repoNames = new ArrayList<>();

                for (RepoEntry r : listOfRepos) {
                    repoNames.add(r.getName());
                }
                userEntry.setStarredRepos(repoNames);
                databaseHelper.writeUser(userEntry);
                currentUser = userEntry;
                notifyUserListener(listener);

            }

            @Override
            public void onFailure(Call<List<RepoEntry>> call, Throwable t) {

            }
        });
    }

    private void notifyUserListener(UserListener listener) {
        if (listener instanceof AuthenticationListener) {
            AuthenticationListener authenticationListener = (AuthenticationListener) listener;
            authenticationListener.onUserAuthenticated();
        }
        else {
            listener.onUserLoaded(currentUser);
        }
    }
}
