package com.tsl.baseapp.model.objects.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kevin Lavi on 9/22/16.
 */

public class UserList {
    @SerializedName("results")
    private List<User> userList;

    @SerializedName("next")
    private String next;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
