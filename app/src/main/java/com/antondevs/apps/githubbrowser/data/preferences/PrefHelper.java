package com.antondevs.apps.githubbrowser.data.preferences;

/**
 * Created by Anton.
 */
public interface PrefHelper {

    void addUserCredentials(String username, String password);

    boolean isAuthenticated();

    void userAuthenticated(boolean status);

    String getUsername();

    String getSecret();
}
