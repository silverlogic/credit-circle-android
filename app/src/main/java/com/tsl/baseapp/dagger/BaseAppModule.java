package com.tsl.baseapp.dagger;

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

}
