package com.antondevs.apps.githubbrowser.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.ui.user.UserActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView {

    public static final String INTENT_EXTRA_KEY = "user_login_name";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView errorMessage;

    private LoginContract.LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.login_username_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);
        loginButton = findViewById(R.id.login_button_login);
        errorMessage = findViewById(R.id.login_error_message_text_view);

        presenter = new LoginPresenterImp(this);

        presenter.loginWithStoredCredentials();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.authenticateUser(usernameEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });
    }

    @Override
    public void requestAuthentication() {
        displayLoginViews();
    }

    @Override
    public void onUserAuthenticated() {
        Intent activityIntent = new Intent(this, UserActivity.class);
        activityIntent.putExtra(INTENT_EXTRA_KEY, usernameEditText.getText().toString().trim());
        startActivity(activityIntent);
    }

    @Override
    public void displayErrorMessage() {
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void displayLoginViews() {
        usernameEditText.setVisibility(View.VISIBLE);
        passwordEditText.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        presenter.destroyPresenter();
        super.onDestroy();
    }
}
