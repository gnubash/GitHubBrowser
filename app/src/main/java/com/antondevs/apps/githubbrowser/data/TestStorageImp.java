package com.antondevs.apps.githubbrowser.data;

/**
 * Created by Anton on 6/30/18.
 */
public class TestStorageImp implements MainStorage {

    private static volatile TestStorageImp UNIQUE_INSTANCE = null;

    private TestStorageImp() {
    }

    public static TestStorageImp getInstance() {
        if (UNIQUE_INSTANCE == null) {
            synchronized (TestStorageImp.class) {
                if (UNIQUE_INSTANCE == null) {
                    UNIQUE_INSTANCE = new TestStorageImp();
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
