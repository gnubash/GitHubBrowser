package com.antondevs.apps.githubbrowser.utilities;

import android.util.Log;

/**
 * Created by Anton.
 */
public final class Methods {

    private static final String LOGTAG = Methods.class.getSimpleName();

    private Methods() {

    }

    public static String getLastPageFromLinkHeader(String linkHeader) {

        int indexOfLastRel = linkHeader.indexOf(">; rel=\"last\"");

        String searchCriteria = "page=";

        int indexOfLastPage = linkHeader.lastIndexOf(searchCriteria);

        if (indexOfLastRel == -1 || indexOfLastPage == -1) {
            return "";
        }

        String substring = linkHeader.substring(indexOfLastPage + searchCriteria.length(), indexOfLastRel);
        Log.d(LOGTAG, "getLastPageFromLinkHeader substring = " + substring);
        return substring;
    }

    public static String getNextPageFromLinkHeader(String linkHeader) {

        String nextPageRel = ">; rel=\"next\"";

        if (linkHeader == null || !linkHeader.contains(nextPageRel)) {
            return "";
        }

        String splitHeader = linkHeader.substring(0,linkHeader.indexOf(nextPageRel));

        String searchCriteria = "page=";

        String substring = splitHeader.substring(splitHeader.lastIndexOf(searchCriteria) + searchCriteria.length());
        Log.d(LOGTAG, "getNextPageFromLinkHeader substring = " + substring);
        return substring;
    }
}
