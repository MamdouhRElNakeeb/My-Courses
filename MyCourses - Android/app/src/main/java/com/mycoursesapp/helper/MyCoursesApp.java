package com.mycoursesapp.helper;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.applikeysolutions.library.Authentication;

/**
 * Created by mamdouhelnakeeb on 2/15/18.
 */

public class MyCoursesApp extends Application {

    private final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        Authentication.init(getBaseContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d(TAG, "attachBaseContext");
        MultiDex.install(this);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
        Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
}
