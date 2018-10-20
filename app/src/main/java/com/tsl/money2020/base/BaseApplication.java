package com.tsl.money2020.base;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tsl.money2020.BuildConfig;
import com.tsl.money2020.R;
import com.tsl.money2020.dagger.BaseAppComponent;
import com.tsl.money2020.dagger.DaggerBaseAppComponent;

import net.danlew.android.joda.JodaTimeAndroid;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();

        Fabric.with(this, new Crashlytics());
        JodaTimeAndroid.init(this);
        // Initialize the Branch object
        Branch.getAutoInstance(this);

        // Add custom font file here
        // Font goes into /assets/fonts folder and replace patch below
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static BaseAppComponent getBaseComponents() {
        return baseComponent;
    }
}
