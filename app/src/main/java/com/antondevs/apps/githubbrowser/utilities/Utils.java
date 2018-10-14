package com.antondevs.apps.githubbrowser.utilities;

import android.util.Log;

/**
 * Created by Anton.
 */
public final class Utils {

    private static final String LOGTAG = Utils.class.getSimpleName();

    private Utils() {

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
        Log.d(LOGTAG, "getNextPageFromLinkHeader splitHeader = " + splitHeader);

        String searchCriteria = "page=";
        String searchCriteriaPerPage = "&per_page=";

        if (splitHeader.contains(searchCriteriaPerPage)) {

            String substring = splitHeader.substring(splitHeader.lastIndexOf("?page=") + "?page=".length(),
                    splitHeader.lastIndexOf(searchCriteriaPerPage));

            Log.d(LOGTAG, "getNextPageFromLinkHeader.if substring = " + substring);

            return substring;
        }

        String substring = splitHeader.substring(splitHeader.lastIndexOf(searchCriteria) + searchCriteria.length());
        Log.d(LOGTAG, "getNextPageFromLinkHeader substring = " + substring);
        return substring;
    }

    public static String getNextPageRel(String linkHeader) {
        String nextPageRel = ">; rel=\"next\"";
        String urlStart = "https";

        if (linkHeader == null || !linkHeader.contains(nextPageRel)) {
            return "";
        }

        String linkHeaderSplitted = linkHeader.substring(0, linkHeader.indexOf(nextPageRel));
        Log.d(LOGTAG, "getNextPageRel linkHeaderSplitted = " + linkHeaderSplitted);
        String nextPageUrl = linkHeaderSplitted.substring(linkHeaderSplitted.lastIndexOf(urlStart));
        Log.d(LOGTAG, "getNextPageRel nextPageUrl = " + nextPageUrl);
        return nextPageUrl;
    }
}
