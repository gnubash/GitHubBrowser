package com.antondevs.apps.githubbrowser.data;

import com.antondevs.apps.githubbrowser.login.LoginContract;

/**
 * Created by Anton on 6/30/18.
 */
public class TestInteractorImp implements MainInteractor{

    private MainInteractor.Listener callbackListener;

    public TestInteractorImp(MainInteractor.Listener presenter) {
        callbackListener = presenter;
    }

    @Override
    public void checkCredentials() {
        callbackListener.onAuthenticationRequered();
    }

    @Override
    public void performAuthentication(String username, String password) {
        callbackListener.onAuthenticationFailed();
    }
}
