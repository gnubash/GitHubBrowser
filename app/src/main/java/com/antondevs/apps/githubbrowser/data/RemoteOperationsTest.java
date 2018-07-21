package com.antondevs.apps.githubbrowser.data;

import android.content.SharedPreferences;
import android.util.Log;

import com.antondevs.apps.githubbrowser.data.database.UserEntry;
import com.antondevs.apps.githubbrowser.data.preferences.PrefHelper;
import com.antondevs.apps.githubbrowser.data.preferences.PrefHelperImp;
import com.antondevs.apps.githubbrowser.data.remote.APIService;
import com.antondevs.apps.githubbrowser.data.remote.RemoteAPIService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Anton.
 */
public class RemoteOperationsTest implements MainInteractor {

    private static final String LOGTAG = RemoteOperationsTest.class.getSimpleName();

//    private static volatile RemoteOperationsTest UNIQUE_INSTANCE = null;

    private PrefHelper prefHelper;
    private RemoteAPIService apiService;

    public RemoteOperationsTest(SharedPreferences sharedPreferences) {
        prefHelper = new PrefHelperImp(sharedPreferences);


    }

//    public static RemoteOperationsTest getInstance() {
//        if (UNIQUE_INSTANCE == null) {
//            synchronized (RemoteOperationsTest.class) {
//                if (UNIQUE_INSTANCE == null) {
//                    UNIQUE_INSTANCE = new RemoteOperationsTest();
//                }
//            }
//        }
//        return UNIQUE_INSTANCE;
//    }

//    public static RemoteOperationsTest getInstance(SharedPreferences sharedPreferences) {
//        if (UNIQUE_INSTANCE == null) {
//            synchronized (RemoteOperationsTest.class) {
//                if (UNIQUE_INSTANCE == null) {
//                    UNIQUE_INSTANCE = new RemoteOperationsTest();
//                    UNIQUE_INSTANCE.prefHelper = new PrefHelperImp(sharedPreferences);
//                }
//            }
//        }
//        return UNIQUE_INSTANCE;
//    }

    @Override
    public void checkCredentials(AuthenticationListener listener) {
        if (prefHelper.isAuthenticated()) {
            performAuthentication(prefHelper.getUsername(), prefHelper.getSecret(), listener);
        }
        else {
            listener.onAuthenticationRequered();
        }
    }

    @Override
    public void performAuthentication(final String username, final String password, final AuthenticationListener listener) {
        if (apiService == null) {
            apiService = APIService.getService(username, password);
        }
        apiService.loginUser().enqueue(new Callback<UserEntry>() {
            @Override
            public void onResponse(Call<UserEntry> call, Response<UserEntry> response) {
                Log.d(LOGTAG, "Request success onResponse()");
                UserEntry user = response.body();
                prefHelper.addUserCredentials(username, password);
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
