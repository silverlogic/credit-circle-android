package com.tsl.baseapp.model.event;

import com.tsl.baseapp.model.objects.user.User1;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class SignUpSuccessfulEvent {
    private User1 mUser1;

    public SignUpSuccessfulEvent(User1 user1) {
        mUser1 = user1;
    }

    public User1 getUser() {
        return mUser1;
    }
}
