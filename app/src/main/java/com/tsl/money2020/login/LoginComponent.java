package com.tsl.money2020.login;

import com.tsl.money2020.dagger.BaseAppComponent;
import com.tsl.money2020.dagger.BaseAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kevinlavi on 4/26/16.
 */
@Singleton
@Component(modules = BaseAppModule.class,
        dependencies = BaseAppComponent.class)
public interface LoginComponent {

    public LoginPresenter presenter();
}