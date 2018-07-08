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
        listener.onUserAuthenticated();
    }

    @Override
    public void queryUser(UserListener listener, String loginName) {

    }

    @Override
    public void queryUsers(SearchListener listener, String loginName) {

    }

    @Override
    public void queryRepo(RepoListener listener, String repoName) {

    }

    @Override
    public void queryFollowers(SearchListener listener, String loginName) {

    }

    @Override
    public void queryFollowing(SearchListener listener, String loginName) {

    }

    @Override
    public void queryContributors(SearchListener listener, String repoName) {

    }
}
