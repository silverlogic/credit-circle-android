package com.tsl.creditcircle.utils.pushnotifications;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevinlavi on 11/17/17.
 */

public class FCMDeviceBody {
    @SerializedName("device_id")
    private final String deviceId = null;

    @SerializedName("registration_id")
    private String registrationId;

    @SerializedName("active")
    final private boolean active = true;

    @SerializedName("type")
    final private String type = "android";

    public FCMDeviceBody(String registrationId) {
        this.registrationId = registrationId;
    }
}
