package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.utilities.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anton.
 */
public class APIService {

    private static final String GITGUB_API_URL = Constants.URL_GIT_API;

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(GITGUB_API_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder client = new OkHttpClient.Builder();

    public static AuthenticationInterceptor authenticationInterceptor = null;

    public static RemoteAPIService getService(String username, String pass) {
        if (!client.interceptors().contains(authenticationInterceptor)) {
            authenticationInterceptor = new AuthenticationInterceptor(username, pass);
            client.addInterceptor(loggingInterceptor);
            client.addInterceptor(authenticationInterceptor);
            retrofitBuilder.client(client.build());
            retrofit = retrofitBuilder.build();
        }
        return retrofit.create(RemoteAPIService.class);
    }

}
