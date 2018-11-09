package com.antondevs.apps.githubbrowser.ui.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.data.MainStorage;
import com.antondevs.apps.githubbrowser.data.MainStorageImp;
import com.antondevs.apps.githubbrowser.data.database.GitHubBrowserDatabase;
import com.antondevs.apps.githubbrowser.databinding.ActivityLoginBinding;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView {

    private static final String LOGTAG = LoginActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_USER_LOGIN_KEY = "user_login_name";

    private ActivityLoginBinding binding;

    private LoginContract.LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        Log.d(LOGTAG, "onCreate()");

        GitHubBrowserDatabase database = GitHubBrowserDatabase.getDatabaseInstance(this);

        MainStorage storage = MainStorageImp.getInstance();

        storage.setDatabaseHelper(database);

        presenter = new LoginPresenterImp(this, storage);

        presenter.loginWithStoredCredentials();

        binding.loginButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.authenticateUser(binding.loginUsernameEditText.getText().toString().trim(),
                        binding.loginPasswordEditText.getText().toString().trim());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void requestAuthentication() {
        showViews();
    }

    @Override
    public void onUserAuthenticated() {
        Intent activityIntent = new Intent(this, UserActivity.class);
        activityIntent.putExtra(INTENT_EXTRA_USER_LOGIN_KEY, presenter.getLogedUsername());
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);
        finishAffinity();
    }

    @Override
    public void showAuthErrorMsg() {
        showError(getString(R.string.login_screen_error_message_text));
    }

    @Override
    public void showNetworkErrorMsg() {
        showError(getString(R.string.login_screen_error_msg_network_issue));
    }

    @Override
    public void showLoading() {
        binding.loginProgressBar.setVisibility(View.VISIBLE);
        binding.loginViewContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        presenter.destroyPresenter();
        super.onDestroy();
    }

    @Override
    public void showViews() {
        binding.loginProgressBar.setVisibility(View.GONE);
        binding.loginViewContainer.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        showViews();
        binding.loginErrorMessageTextView.setVisibility(View.VISIBLE);
        binding.loginErrorMessageTextView.setText(message);
    }

}
