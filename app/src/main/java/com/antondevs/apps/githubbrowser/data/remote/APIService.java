package com.antondevs.apps.githubbrowser.data.remote;

import com.antondevs.apps.githubbrowser.utilities.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anton.
 */
public class APIService {

    private static final String GITGUB_API_URL = Constants.URL_GIT_API;

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(GITGUB_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder client = new OkHttpClient.Builder();

    private static BasicAuthInterceptor authenticationInterceptor = new BasicAuthInterceptor();

    public static RemoteAPIService getService() {
        if (!client.interceptors().contains(loggingInterceptor)) {
            client.addInterceptor(loggingInterceptor);
            client.addInterceptor(authenticationInterceptor);
            retrofitBuilder.client(client.build());
            retrofit = retrofitBuilder.build();
        }
        return retrofit.create(RemoteAPIService.class);
    }

    public static void setCredentials(String basicCredentials) {
        if(client.interceptors().contains(authenticationInterceptor)) {
            BasicAuthInterceptor interceptor = (BasicAuthInterceptor) client.interceptors().get(1);
            interceptor.setCredentials(basicCredentials);
        }
    }

    public static void clearAuthCredentials() {
        if(client.interceptors().contains(authenticationInterceptor)) {
            BasicAuthInterceptor interceptor = (BasicAuthInterceptor) client.interceptors().get(1);
            interceptor.clearCredentials();
        }
    }

}
