package com.antondevs.apps.githubbrowser.data;

/**
 * Created by Anton on 6/30/18.
 */
public class TestInteractorImp implements MainInteractor{

    private static volatile TestInteractorImp UNIQUE_INSTANCE = null;

    private TestInteractorImp() {
    }

    public static TestInteractorImp getInstance() {
        if (UNIQUE_INSTANCE == null) {
            synchronized (TestInteractorImp.class) {
                if (UNIQUE_INSTANCE == null) {
                    UNIQUE_INSTANCE = new TestInteractorImp();
                }
            }
        }
        return UNIQUE_INSTANCE;
    }

    @Override
    public void checkCredentials(AuthenticationListener presenter) {
        presenter.onAuthenticationRequered();
    }

    @Override
    public void performAuthentication(String username, String password, AuthenticationListener listener) {
        listener.onAuthenticationFailed();
    }
}
