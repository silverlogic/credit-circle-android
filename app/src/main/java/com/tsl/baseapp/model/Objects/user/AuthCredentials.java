package com.tsl.baseapp.model.Objects.user;

/**
 * Created by kevinlavi on 4/26/16.
 */
public class AuthCredentials {

    String username;
    String password;

    public AuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

}