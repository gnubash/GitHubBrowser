package com.antondevs.apps.githubbrowser.data.remote;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Anton.
 */
class BasicAuthInterceptor implements Interceptor {

    private String basicAuthCredentials;

    BasicAuthInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (basicAuthCredentials != null) {
            Request authentication = request.newBuilder()
                    .addHeader("Authorization", basicAuthCredentials)
                    .build();
            return chain.proceed(authentication);
        }

        return chain.proceed(request);
    }

    void setCredentials(String basicAuthCredentials) {
        this.basicAuthCredentials = basicAuthCredentials;
    }

    void clearCredentials() {
        this.basicAuthCredentials = null;
    }
}
