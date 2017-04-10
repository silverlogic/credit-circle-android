package com.tsl.baseapp.utils;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.R;

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

    public static final String TWITTER_USER = "twitterUser";

    public static final String getToken(){
        if (Hawk.isBuilt() && Hawk.get(TOKEN) != null){
            return  "Token " + Hawk.get(TOKEN);
        }
        else {
            // we do this to avoid testing hawk during unit testing
            return "token for testing";
        }
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
