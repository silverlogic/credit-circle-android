package com.tsl.baseapp.utils;

import android.content.Context;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

/**
 * Created by Kevin on 9/17/15.
 */
public class Constants {
    private Constants() {
    }

    public static final String PRODUCTION_URL = "http://api.fundthis.com/v1/";
    public static final String STAGING_URL = "http://api.staging.fundthis.com/";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public static final String TOKEN = "token";
    public static final String ERROR = "error";

    public static final String USER = "user";

    public static final String URL = "url";
    public static final String TITLE = "title";

    public static final String AUTHORIZATION = "Authorization";

    public static final String FILENAME = "filename";
    public static final String PICTURE_TYPE = "picture_type";
    public static final String PICTURE_CAPTURE = "capture";
    public static final String PICTURE_PICK = "pick";

    public static final String getToken(Context ctx){
        String token = "Token " + Hawk.get(TOKEN);
        return token;
    }

    public static final Toast makeToast(Context ctx, int msg){
        return  Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
    }

}
