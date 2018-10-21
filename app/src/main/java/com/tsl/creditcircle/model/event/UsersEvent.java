package com.tsl.creditcircle.model.event;

import com.tsl.creditcircle.model.objects.api.PaginatedResponse;
import com.tsl.creditcircle.model.objects.user.User;

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
