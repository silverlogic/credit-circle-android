package com.tsl.baseapp.model.Objects.user;

import com.tsl.baseapp.model.Objects.user.AuthCredentials;
import com.tsl.baseapp.model.Objects.user.User;

import rx.Observable;

/**
 * Created by kevinlavi on 4/26/16.
 */
public interface UserManager {
    Observable<User> doLogin(AuthCredentials credentials);

    User getCurrentUser();

    boolean isUserAuthenticated();
}
