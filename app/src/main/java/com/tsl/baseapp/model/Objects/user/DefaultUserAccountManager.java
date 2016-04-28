package com.tsl.baseapp.model.Objects.user;

import javax.security.auth.login.LoginException;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kevinlavi on 4/26/16.
 */
public class DefaultUserAccountManager implements UserManager {

    private User currentUser;

    /**
     * Returns the Account observable
     */
    @Override public Observable<User> doLogin(AuthCredentials credentials) {

        return Observable.just(credentials).flatMap(new Func1<AuthCredentials, Observable<User>>() {
            @Override public Observable<User> call(AuthCredentials credentials) {

                try {
                    // Simulate network delay
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (credentials.getUsername().equals("kl@tsl.io") && credentials.getPassword().equals("1234")) {
                    currentUser = new User();
                    return Observable.just(currentUser);
                }

                return Observable.error(new LoginException());
            }
        });
    }

    @Override public User getCurrentUser() {
        return currentUser;
    }

    @Override public boolean isUserAuthenticated() {
        return currentUser != null;
    }
}