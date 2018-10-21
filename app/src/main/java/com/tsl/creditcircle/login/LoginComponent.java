package com.tsl.creditcircle.login;

import com.tsl.creditcircle.dagger.BaseAppComponent;
import com.tsl.creditcircle.dagger.BaseAppModule;

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