package com.antondevs.apps.githubbrowser.data.remote;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Anton.
 */
public class AuthenticationInterceptor implements Interceptor {

    private String credentials

    public AuthenticationInterceptor(String username, String passowrd) {
        credentials = Credentials.basic(username, passowrd);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request authentication = request.newBuilder()
                .addHeader("Authorization", credentials)

        return chain.proceed(authentication);
    }
}
