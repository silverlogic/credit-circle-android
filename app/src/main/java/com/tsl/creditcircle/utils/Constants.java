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

    public static final String CURRENT_LOAN = "currentLoan";
    public static final String CURRENT_CREDIT = "currentCredit";

    public static final String FCM_TOKEN = "FCMtoken";

    public static final String getToken(){
        return "token " + "0e49529c28fa56ed3a26d82a571c3bb06ea9b797";
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
