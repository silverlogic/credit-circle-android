package com.tsl.money2020.model.event;

import com.tsl.money2020.model.objects.api.PaginatedResponse;
import com.tsl.money2020.model.objects.user.User;

import java.util.List;

/**
 * Created by Kevin Lavi on 9/22/16.
 */

public class UsersEvent {
    private PaginatedResponse<User> mUserList;

    public UsersEvent(PaginatedResponse<User> userList) {
        mUserList = userList;
    }

    public List<User> getUserList() {
        return mUserList.getResults();
    }

    public PaginatedResponse<User> getResponse() {
        return mUserList;
    }
}
