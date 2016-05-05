package com.tsl.baseapp.feed;

import com.tsl.baseapp.dagger.BaseAppComponent;
import com.tsl.baseapp.dagger.BaseAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kevinlavi on 5/5/16.
 */
@Singleton
@Component(modules = BaseAppModule.class,
        dependencies = BaseAppComponent.class)
public interface FeedComponent {
    public FeedPresenter presenter();
}