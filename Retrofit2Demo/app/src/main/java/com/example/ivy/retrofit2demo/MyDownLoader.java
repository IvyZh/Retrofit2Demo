package com.example.ivy.retrofit2demo;

import com.squareup.picasso.Downloader;

/**
 * Created by steven on 16/3/17.
 */
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.squareup.picasso.NetworkPolicy;
import java.io.File;
import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/** A {@link Downloader} which uses OkHttp to download images. */
public class MyDownLoader implements Downloader {
    private static OkHttpClient defaultOkHttpClient() {
        OkHttpClient client = OkHttp3Utils.getOkHttpSingletonInstance();
        Log.d("MyOKHttpDownloader", "---->defaultOkHttpClient(): " + client.toString());
        return client;
    }

    private final OkHttpClient client;

    public MyDownLoader() {
        this(defaultOkHttpClient());
    }

    /**
     * Create a new downloader that uses the specified OkHttp instance. A response cache will not be
     * automatically configured.
     */
    public MyDownLoader(OkHttpClient client) {
        this.client = client;
    }

    protected final OkHttpClient getClient() {
        return client;
    }

    @Override public Response load(Uri uri, int networkPolicy) throws IOException {
        CacheControl cacheControl = null;
        if (networkPolicy != 0) {
            if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                cacheControl = CacheControl.FORCE_CACHE;
            } else {
                CacheControl.Builder builder = new CacheControl.Builder();
                if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                    builder.noCache();
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                    builder.noStore();
                }
                cacheControl = builder.build();
            }
        }

        Request.Builder builder = new Request.Builder().url(uri.toString());
        if (cacheControl != null) {
            builder.cacheControl(cacheControl);
        }

        okhttp3.Response response = client.newCall(builder.build()).execute();
        int responseCode = response.code();
        if (responseCode >= 300) {
            response.body().close();
            throw new ResponseException(responseCode + " " + response.message(), networkPolicy,
                    responseCode);
        }

        boolean fromCache = response.cacheResponse() != null;

        ResponseBody responseBody = response.body();
        return new Response(responseBody.byteStream(), fromCache, responseBody.contentLength());
    }

    @Override public void shutdown() {
        okhttp3.Cache cache = client.cache();
        if (cache != null) {
            try {
                cache.close();
            } catch (IOException ignored) {
            }
        }
    }
}


