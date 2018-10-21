package com.tsl.money2020.model.objects;

import com.google.gson.annotations.SerializedName;


public class Friend {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("job")
    String job;

    @SerializedName("stars")
    int stars;

    boolean isInvited;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public int getStars() {
        return stars;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }
}
