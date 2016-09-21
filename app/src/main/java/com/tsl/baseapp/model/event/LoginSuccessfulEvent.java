package com.tsl.baseapp.model.event;

/**
 * Created by kevinlavi on 4/26/16.
 */

import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.User;

/**
 * Fired to inform that the login was successful
 *
 *
 */
public class LoginSuccessfulEvent {

    private String mToken;
    private User mUser;

    public LoginSuccessfulEvent(Token token, User user) {
        mToken = token.getToken();
        mUser = user;
    }

    public String getToken() {
        return mToken;
    }

    public User getUser(){return mUser;}
}