package com.tsl.baseapp.model.event;

import com.tsl.baseapp.model.objects.user.User;

import java.util.List;

/**
 * Created by Kevin Lavi on 3/13/17.
 */

public class BaseEvent {
    private List<User> mUserList;

    public BaseEvent(List<User> userList) {
        mUserList = userList;
    }

    public List<User> getUserList() {
        return mUserList;
    }
}
