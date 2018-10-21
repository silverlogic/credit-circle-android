package com.tsl.creditcircle.model.objects;

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
    int vouchedAmount;

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

    public Friend(int id, String name, String job, int stars, int vouchedAmount) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.stars = stars;
        this.vouchedAmount = vouchedAmount;
    }

    public int getVouchedAmount() {
        return vouchedAmount;
    }
}
