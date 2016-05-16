package com.tsl.baseapp.base;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.dagger.BaseAppComponent;
import com.tsl.baseapp.dagger.DaggerBaseAppComponent;

import timber.log.Timber;

/**
 * Created by kevinlavi on 4/26/16.
 */
public class BaseApplication extends Application {

    private RefWatcher refWatcher;
    private static BaseAppComponent baseComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        baseComponent = DaggerBaseAppComponent.create();
        refWatcher = LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static BaseAppComponent getBaseComponents() {
        return baseComponent;
    }
}
