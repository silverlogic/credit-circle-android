package com.tsl.baseapp.model.event;

import com.tsl.baseapp.model.objects.user.User;

import java.util.List;

/**
 * Created by Kevin Lavi on 9/22/16.
 */

public class UsersEvent {
    private List<User> mUserList;

    public UsersEvent(List<User> userList) {
        mUserList = userList;
    }

    public List<User> getUserList() {
        return mUserList;
    }
}
