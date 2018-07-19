package com.antondevs.apps.githubbrowser.data;

import android.content.SharedPreferences;

import com.antondevs.apps.githubbrowser.data.preferences.PrefHelper;
import com.antondevs.apps.githubbrowser.data.preferences.PrefHelperImp;

/**
 * Created by Anton.
 */
public class RemoteOperationsTest implements MainInteractor {

    private static volatile RemoteOperationsTest UNIQUE_INSTANCE = null;

    private PrefHelper prefHelper;

    private RemoteOperationsTest() {
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

    public static RemoteOperationsTest getInstance(SharedPreferences sharedPreferences) {
        if (UNIQUE_INSTANCE == null) {
            synchronized (RemoteOperationsTest.class) {
                if (UNIQUE_INSTANCE == null) {
                    UNIQUE_INSTANCE = new RemoteOperationsTest();
                    UNIQUE_INSTANCE.prefHelper = new PrefHelperImp(sharedPreferences);
                }
            }
        }
        return UNIQUE_INSTANCE;
    }

    @Override
    public void checkCredentials(AuthenticationListener listener) {
        if (prefHelper.isAuthenticated()) {
            listener.onUserAuthenticated();
        }
        else {
            listener.onAuthenticationRequered();
        }
    }

    @Override
    public void performAuthentication(String username, String password, AuthenticationListener listener) {
        
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
