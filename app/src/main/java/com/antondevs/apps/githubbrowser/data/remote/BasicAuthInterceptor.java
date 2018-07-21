package com.antondevs.apps.githubbrowser.data.remote;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Anton.
 */
public class BasicAuthInterceptor implements Interceptor {

    private String credentials;

    public BasicAuthInterceptor() {
        this.credentials = null;
    }

    public void setCredentials(String username, String password) {
        this.credentials = Credentials.basic(username, password);
    }

    public void clearCredentials() {
        this.credentials = null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (credentials != null) {
            Request authentication = request.newBuilder()
                    .addHeader("Authorization", credentials)
                    .build();
            return chain.proceed(authentication);
        }

        return chain.proceed(request);
    }
}
