package com.tsl.money2020.baseview;

import com.tsl.money2020.dagger.BaseAppComponent;
import com.tsl.money2020.dagger.BaseAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kevinlavi on 5/6/16.
 */
@Singleton
@Component(modules = BaseAppModule.class,
        dependencies = BaseAppComponent.class)
public interface BaseComponant {
    public BasePresenter presenter();
}
