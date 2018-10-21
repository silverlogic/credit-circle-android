package com.tsl.creditcircle.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by Kevin on 9/17/15.
 */
public class Constants {
    private Constants() {
    }

    public static final String PRODUCTION_URL = "https://api.baseapp.tsl.io/v1/";
    public static String STAGING_URL = "https://api.baseapp.tsl.io/v1/";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static final String TOKEN = "token";
    public static final String ERROR = "error";

    public static final String USER_ID = "userId";

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String COLOR = "color";
    public static final String COLOR_DARK = "colorDark";

    public static final String AUTHORIZATION = "Authorization";

    public static final String FILENAME = "filename";
    public static final String PICTURE_TYPE = "picture_type";
    public static final String PICTURE_CAPTURE = "capture";
    public static final String PICTURE_PICK = "pick";

    public static final String FACEBOOK_APP_ID = "973634146036464";
    public static final String LINKEDIN_CLIENT_ID = "781ehwqhni34oe";
    public static final String REDIRECT_URL = "https://app.baseapp.tsl.io/";
    public static final String VIEWED_TUTORIAL = "viewedTutorial";

    public static final String TWITTER_USER = "twitterUser";

    // bluetooth device uuid
    public static final UUID DEVICE_UUID = UUID.fromString("18d876b8-b6d6-503c-92f4-7ff407ddc89c");
    public static final UUID BLUETOOTH_CHARACTERISTIC = UUID.fromString("eb18c3d6-c156-4c9e-9f89-a7ad886dabb6");
    public static final UUID SERVICE_UUID = UUID.fromString("aa59768e-0591-436c-bf8e-ccfe879a3929");

    public static final String CURRENT_LOAN = "currentLoan";

    public static final String FCM_TOKEN = "FCMtoken";

    public static final String getToken(){
        return "token " + "e836b5c3019d6c88148e08504376f136cc7cedb1";
    }

    public static final Toast makeToast(Context ctx, int msg){
        return  Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
    }

    public static String getOAuth2LoginURLForFacebook(Context context, int stringId, String appId) {
        return context.getString(stringId, appId, Constants.REDIRECT_URL);
    }

    public static String getOAuth2LoginURLForLinkedIn(Context context, int stringId, String appId) {
        return context.getString(stringId, appId, Constants.REDIRECT_URL, Utils.getRandomString());
    }
}
