package com.tsl.baseapp.model.Objects.token;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kevin on 10/16/15.
 */
public class Token {
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
