package com.antondevs.apps.githubbrowser.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antondevs.apps.githubbrowser.R;

public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public void requestAuthentication() {

    }

    @Override
    public void onUserAuthenticated() {

    }

    @Override
    public void displayErrorMessage() {

    }
}
