package com.tsl.creditcircle.forgotpassword;

import com.tsl.creditcircle.dagger.BaseAppComponent;
import com.tsl.creditcircle.dagger.BaseAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kevinlavi on 5/6/16.
 */
@Singleton
@Component(modules = BaseAppModule.class,
        dependencies = BaseAppComponent.class)
public interface ForgotPasswordComponent {
    public ForgotPasswordPresenter presenter();
}
