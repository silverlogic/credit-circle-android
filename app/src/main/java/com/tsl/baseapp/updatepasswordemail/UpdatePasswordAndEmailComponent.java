package com.tsl.baseapp.updatepasswordemail;


import com.tsl.baseapp.dagger.BaseAppComponent;
import com.tsl.baseapp.dagger.BaseAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kevinlavi on 5/6/16.
 */
@Singleton
@Component(modules = BaseAppModule.class,
        dependencies = BaseAppComponent.class)
public interface UpdatePasswordAndEmailComponent {
    public UpdatePasswordAndEmailPresenter presenter();
}
