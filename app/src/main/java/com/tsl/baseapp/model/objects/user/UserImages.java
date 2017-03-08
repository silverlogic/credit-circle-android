package com.tsl.baseapp.model.objects.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class UserImages implements Serializable {
    @SerializedName("small")
    private String small;

    @SerializedName("full_size")
    private String full_size;

    @SerializedName("url")
    private String url;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getFull_size() {
        return full_size;
    }

    public void setFull_size(String full_size) {
        this.full_size = full_size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
