package com.tsl.money2020.model.objects.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by kevinlavi on 5/3/16.
 */
@RealmClass
public class UserImages extends RealmObject implements Serializable {
    @SerializedName("small")
    String small;

    @SerializedName("full_size")
    String full_size;

    @SerializedName("url")
    String url;

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
