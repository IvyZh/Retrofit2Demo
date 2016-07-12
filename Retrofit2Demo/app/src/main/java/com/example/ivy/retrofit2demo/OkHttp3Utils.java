package com.example.ivy.retrofit2demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by steven on 16/3/17.
 */
public class OkHttp3Utils {
    private static OkHttpClient okHttpClient = null;

    public static OkHttpClient getOkHttpSingletonInstance() {
        if (okHttpClient == null) synchronized (OkHttpClient.class) {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                        .cookieJar(new CookieJar() {
                            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                            @Override
                            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                cookieStore.put(url, cookies);
                            }

                            @Override
                            public List<Cookie> loadForRequest(HttpUrl url) {
                                List<Cookie> cookies = cookieStore.get(url);
                                return cookies != null ? cookies : new ArrayList<Cookie>();
                            }
                        })
                        .build();
            }
        }
        return okHttpClient;
    }
}
