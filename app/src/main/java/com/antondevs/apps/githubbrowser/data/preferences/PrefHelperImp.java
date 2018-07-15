package com.antondevs.apps.githubbrowser.data.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Anton.
 */
public class PrefHelperImp implements PrefHelper{

    private static final String KEY_USER_LOGIN_NAME = "key_user_login_name";
    private static final String KEY_USER_PASSWORD = "key_user_pass";
    private static final String KEY_IS_AUTHENTICATED = "key_is_auth";

    private SharedPreferences sharedPreferences;

    private static volatile PrefHelperImp uniqueInstance;

    private PrefHelperImp(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private static PrefHelperImp getPrefHelperInstance(SharedPreferences sharedPreferences) {
        if (uniqueInstance == null) {
            synchronized (PrefHelperImp.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PrefHelperImp(sharedPreferences);
                }
            }
        }
        return uniqueInstance;
    }

    @Override
    public void addUserCredentials(String username, String password) {
        sharedPreferences.edit().putString(KEY_USER_LOGIN_NAME, username).apply();
        sharedPreferences.edit().putString(KEY_USER_PASSWORD, password).apply();
    }

    @Override
    public boolean isAuthenticated() {
        return sharedPreferences.getBoolean(KEY_IS_AUTHENTICATED, false);
    }

    @Override
    public void userAuthenticated(boolean status) {
        sharedPreferences.edit().putBoolean(KEY_IS_AUTHENTICATED, status).apply();
    }
}
