package com.antondevs.apps.githubbrowser.data.database.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Anton.
 */
public class ReposListConverter {

    public static List<String> getListFromString(String reposList) {
        List<String> repos;

        String [] arrayOfStrings = reposList.split(",");

        repos = Arrays.asList(arrayOfStrings);

        return repos;
    }

    public static String getStringFromRepoList(List<String> reposList) {
        StringBuilder reposAsString = new StringBuilder();

        for (String s : reposList) {
            reposAsString.append(s);
            reposAsString.append(',');
        }

        return reposAsString.toString();
    }
}
