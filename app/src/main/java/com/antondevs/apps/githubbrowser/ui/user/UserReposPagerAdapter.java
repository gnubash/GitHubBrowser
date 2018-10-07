package com.antondevs.apps.githubbrowser.ui.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Anton.
 */
public class UserReposPagerAdapter extends FragmentPagerAdapter {

    private static final String LOGTAG = UserReposPagerAdapter.class.getSimpleName();
    public static final String TAB_1_TITLE = "Owned repos";
    public static final String TAB_2_TITLE = "Starred repos";

    private UserReposFragment ownedFragment;
    private UserReposFragment starredFragment;

    public UserReposPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.d(LOGTAG, "UserReposPagerAdapter");

    }

    @Override
    public Fragment getItem(int position) {
        Log.d(LOGTAG, "getItem = " + position);
        switch (position) {
            case 0:
                ownedFragment = new UserReposFragment();
                return ownedFragment;
            case 1:
                starredFragment = new UserReposFragment();
                return starredFragment;
            default:
                throw new IllegalStateException("getItem() called for invalid position.");
        }
    }

    @Override
    public int getCount() {
        Log.d(LOGTAG, "getCount");
        return 2;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d(LOGTAG, "notifyDataSetChanged");
        super.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(LOGTAG, "getPageTitle " + position);
        switch (position) {
            case 0:
                return TAB_1_TITLE;
            case 1:
                return TAB_2_TITLE;
            default:
                throw new IllegalStateException("getPageTitle() called for invalid position.");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(LOGTAG, "instantiateItem " + position);
        switch (position) {
            case 0:
                ownedFragment = (UserReposFragment) super.instantiateItem(container, position);
                return ownedFragment;
            case 1:
                starredFragment = (UserReposFragment) super.instantiateItem(container, position);
                return starredFragment;
            default:
                throw new IllegalStateException("instantiateItem() called for invalid position.");
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Log.d(LOGTAG, "getItemPosition " + System.identityHashCode(object));
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        Log.d(LOGTAG, "destroyItem " + position);

        switch (position) {
            case 0:
                super.destroyItem(container, position, ownedFragment);
                break;
            case 1:
                super.destroyItem(container, position, starredFragment);
                break;
            default:
                throw new IllegalStateException("getItemPosition() called for invalid position.");
        }
    }

    void setOwnedReposList(List<String> ownedReposList) {
        ownedFragment.setReposList(ownedReposList);
    }

    void setStarredReposList(List<String> starredReposList) {
        starredFragment.setReposList(starredReposList);
    }


}
