package com.tsl.baseapp.base;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.dagger.BaseAppComponent;
import com.tsl.baseapp.dagger.DaggerBaseAppComponent;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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

        // Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        Realm.getDefaultInstance();

        
        baseComponent = DaggerBaseAppComponent.create();
        refWatcher = LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();

        Fabric.with(this, new Crashlytics());
        JodaTimeAndroid.init(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static BaseAppComponent getBaseComponents() {
        return baseComponent;
    }
}
