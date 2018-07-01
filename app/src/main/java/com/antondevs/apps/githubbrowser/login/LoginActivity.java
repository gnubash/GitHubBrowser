package com.antondevs.apps.githubbrowser.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.antondevs.apps.githubbrowser.R;
import com.antondevs.apps.githubbrowser.data.TestInteractorImp;

public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView {

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

        presenter = new LoginPresenterImp(this, new TestInteractorImp());

        presenter.loginWithStoredCredentials();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.authenticateUser("fakeuser", "fakepass");
            }
        });
    }

    @Override
    public void requestAuthentication() {
        displayLoginViews();
    }

    @Override
    public void onUserAuthenticated() {

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
