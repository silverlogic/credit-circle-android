package com.tsl.baseapp.model.event;

/**
 * Created by kevinlavi on 4/26/16.
 */

/**
 * Fired to inform that the login was successful
 *
 *
 */
public class LoginSuccessfulEvent {

    private String mToken;

    public LoginSuccessfulEvent(String token) {
        mToken = token;
    }

    public String getToken() {
        return mToken;
    }
}