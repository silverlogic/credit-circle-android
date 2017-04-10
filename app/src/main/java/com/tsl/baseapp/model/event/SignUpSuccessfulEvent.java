package com.tsl.baseapp.model.event;

import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.User;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class SignUpSuccessfulEvent {
    private User mUser;

    public SignUpSuccessfulEvent(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }
}
