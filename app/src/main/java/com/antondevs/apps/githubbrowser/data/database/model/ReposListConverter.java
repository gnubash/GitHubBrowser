package com.antondevs.apps.githubbrowser.data.database.model;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton.
 */
public class ReposListConverter {

    @TypeConverter
    public static List<String> getListFromString(String reposList) {
        List<String> repos;

        String [] arrayOfStrings = reposList.split(",");

        repos = new ArrayList<>(Arrays.asList(arrayOfStrings));

        return repos;
    }

    @TypeConverter
    public static String getStringFromRepoList(List<String> reposList) {
        StringBuilder reposAsString = new StringBuilder();

        for (String s : reposList) {
            reposAsString.append(s);
            reposAsString.append(',');
        }

        return reposAsString.toString();
    }
}
