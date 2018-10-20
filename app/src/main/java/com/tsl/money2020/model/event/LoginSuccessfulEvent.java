package com.tsl.money2020.model.event;

/**
 * Created by kevinlavi on 4/26/16.
 */

import com.tsl.money2020.model.objects.user.User;

/**
 * Fired to inform that the login was successful
 *
 *
 */
public class LoginSuccessfulEvent {

    private User mUser;

    public LoginSuccessfulEvent(User user) {
        mUser = user;
    }

    public User getUser(){return mUser;}
}