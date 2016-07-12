package com.example.ivy.retrofit2demo;

import android.app.Application;
import android.graphics.Bitmap;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Created by Ivy on 2016/7/12.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        initOkHttp();
        
        initPicasso();
    }

    private void initOkHttp() {
        OkHttpClient client = OkHttp3Utils.getOkHttpSingletonInstance();


    }

    private void initPicasso() {
        Picasso picasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(10 << 20))
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .downloader(new MyDownLoader())
                .indicatorsEnabled(true)
                .build();

        Picasso.setSingletonInstance(picasso);


    }
}
