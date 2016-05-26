package com.tsl.baseapp.model.objects.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class UserImages {
    @SerializedName("small_url")
    private String small_url;

    @SerializedName("large_url")
    private String large_url;

    @SerializedName("url")
    private String url;

    public String getSmall_url() {
        return small_url;
    }

    public void setSmall_url(String small_url) {
        this.small_url = small_url;
    }

    public String getLarge_url() {
        return large_url;
    }

    public void setLarge_url(String large_url) {
        this.large_url = large_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
