package com.tsl.baseapp.dagger;

import com.tsl.baseapp.model.Objects.user.DefaultUserAccountManager;
import com.tsl.baseapp.model.Objects.user.UserManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by kevinlavi on 4/26/16.
 */
@Module
public class BaseAppModule {

    // Singletons
    private UserManager accountManager = new DefaultUserAccountManager();

    @Singleton
    @Provides
    public UserManager providesAccountManager() {
        return accountManager;
    }

    @Singleton @Provides
    public EventBus providesEventBus() {
        return EventBus.getDefault();
    }

}
