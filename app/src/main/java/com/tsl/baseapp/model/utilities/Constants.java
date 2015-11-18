package com.tsl.baseapp.Model.Utilities;

import android.content.Context;

/**
 * Created by Kevin on 9/17/15.
 */
public class Constants {

    public static final String BASE_URL = "http://api.staging.fundthis.com/v1/";

    public static final String getToken(Context ctx){
        String token = "Token " + SaveSharedPreference.getUserName(ctx);
        return token;
    }

    public static final String isUser(Context ctx){
        if (SaveSharedPreference.isLoggedIn(ctx)){
            return "Token " + SaveSharedPreference.getUserName(ctx);
        }
        else {
            return null;
        }
    }

}
