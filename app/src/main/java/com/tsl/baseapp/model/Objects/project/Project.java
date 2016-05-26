package com.tsl.baseapp.model.objects.project;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kevin on 9/17/15.
 */
public class Project {

    @SerializedName("id")
    private int id;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("image_url")
    private String image_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}
