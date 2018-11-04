package com.antondevs.apps.githubbrowser.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.antondevs.apps.githubbrowser.ui.login.LoginActivity;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

/**
 * Created by Anton.
 */
public abstract class AbsGitHubActivity extends AppCompatActivity implements CommonViewBehavior {

    private static final String LOGTAG = AbsGitHubActivity.class.getSimpleName();

    @Override
    public void goToUser(String user) {
        Log.d(LOGTAG, "goToUser");
        Intent userActivityIntent = new Intent(this, UserActivity.class);
        userActivityIntent.putExtra(LoginActivity.INTENT_EXTRA_USER_LOGIN_KEY, user);
        startActivity(userActivityIntent);
    }

    @Override
    public void goToLogin() {
        Log.d(LOGTAG, "goToLogin");
        Intent userActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(userActivityIntent);
        finishAffinity();
    }
}
