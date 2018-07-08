package com.antondevs.apps.githubbrowser.ui.user;

/**
 * Created by Anton on 7/8/18.
 */
public class UserPresenterImp implements UserContract.UserPresenter {

    private String mUserLoginName;

    private final UserContract.UserView mView;

    public UserPresenterImp(String userLoginName, UserContract.UserView view) {
        mUserLoginName = userLoginName;
        mView = view;
    }

    @Override
    public void loadPresenter() {

    }

    @Override
    public void getFollowers(String username) {

    }

    @Override
    public void getFollowing(String username) {

    }

    private void loadTestUserFromUtils() {

    }
}
