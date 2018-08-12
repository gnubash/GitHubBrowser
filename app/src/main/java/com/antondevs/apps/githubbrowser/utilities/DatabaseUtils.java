package com.antondevs.apps.githubbrowser.utilities;

import com.antondevs.apps.githubbrowser.data.database.model.RepoEntry;
import com.antondevs.apps.githubbrowser.data.database.model.UserEntry;

import java.util.List;
import java.util.Random;

/**
 * Created by Anton on 6/29/18.
 */
public final class DatabaseUtils {

    private static String [] names = {"mini", "roger", "dans", "helicopter", "restless", "george"};

    private static String [] repoNames = {"android_timing", "android_app_bar", "rpi3 io", "stackable windows",
            "custom tiles", "window like tiles", "drawable customs", "rpi io hat", "ohmyzsh"};

    private static int [] integer_values = {23, 67, 2, 18, 45, 33, 29, 56, 100, 174, 99, 39};

    public static UserEntry generateUser() {
        UserEntry user = new UserEntry();
        Random random = new Random();

        user.setLogin(names[random.nextInt(names.length)]);
        user.setFollowers(integer_values[random.nextInt(integer_values.length)]);
        user.setFollowing(integer_values[random.nextInt(integer_values.length)]);
        return user;
    }

    public static RepoEntry generateRepor() {
        RepoEntry repo = new RepoEntry();
        Random random = new Random();

        repo.setLogin(names[random.nextInt(names.length)]);
        repo.setName(repoNames[random.nextInt(repoNames.length)]);
        repo.setForks(integer_values[random.nextInt(integer_values.length)]);
        repo.setWatchers(integer_values[random.nextInt(integer_values.length)]);
        return repo;
    }

    public static List<UserEntry> createEntriesList() {
        return null;
    }
}
