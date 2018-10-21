package com.tsl.creditcircle.model.event;

import com.tsl.creditcircle.model.objects.user.User;

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
