package com.test;

import android.app.Application;

import com.imageloader.ImageLoaderUtils;

/**
 * @author: cgf
 * Date: 2016/7/6.
 */
public class MainApplication extends Application{

    private static MainApplication instance;
    public static MainApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = MainApplication.this;
        ImageLoaderUtils.initImageLoader(this);
    }
}
