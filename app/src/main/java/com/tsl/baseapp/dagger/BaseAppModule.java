package com.tsl.baseapp.dagger;

import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by kevinlavi on 4/26/16.
 */
@Module
public class BaseAppModule {
    @Singleton @Provides
    public EventBus providesEventBus() {
        return EventBus.getDefault();
    }
    @Singleton @Provides
    public BaseApi providesApi() {
        return new BaseApiManager().getAppApi();
    }

}
