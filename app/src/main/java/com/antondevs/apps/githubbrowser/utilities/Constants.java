package com.antondevs.apps.githubbrowser.utilities;

import java.util.concurrent.TimeUnit;

/**
 * Created by Anton.
 */
public class Constants {

    public static final String URL_GIT_API = "https://api.github.com/";

    public static final int CACHE_SIZE = 10 * 1024 * 1024;

    public static final long CACHE_MAX_AGE = TimeUnit.HOURS.toSeconds(24);
}
