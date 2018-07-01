package com.antondevs.apps.githubbrowser.data;

/**
 * Created by Anton on 6/30/18.
 */
public class TestInteractorImp implements MainInteractor{

    private AuthenticationListener callbackListener;

    public TestInteractorImp(AuthenticationListener presenter) {
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
