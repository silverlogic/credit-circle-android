package com.tsl.baseapp.model.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tsl.baseapp.model.Objects.user.User2;

/**
 * Created by Kevin on 10/7/15.
 */
public class SaveSharedPreference {

    static final String PREF_TOKEN= "token";
    static final String LOG_IN_STATUS= "isLoggedIn";
    static final String CURRENT_USER = "user";
    static final String PASSWORD= "password";
    static final String PUSH_TOKEN = "pushToken";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_TOKEN, token);
        editor.putBoolean(LOG_IN_STATUS, true);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_TOKEN, "");
    }

    public static void clearToken(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(LOG_IN_STATUS, false);
        editor.clear(); //clear all stored data
        editor.commit();
    }

    public static boolean isLoggedIn(Context ctx){
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getBoolean(LOG_IN_STATUS, false);
    }

    public static void setCurrentUser(User2 user2, Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user2); // myObject - instance of MyObject
        editor.putString(CURRENT_USER, json);
        editor.commit();
    }

    public static User2 getCurrentUser(Context ctx){
        Gson gson = new Gson();
        String json = getSharedPreferences(ctx).getString(CURRENT_USER, "");
        User2 mUser2 = gson.fromJson(json, User2.class);

        return mUser2;
    }

    public static void setCurrentPassword(Context ctx, String pass){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PASSWORD, pass);
        editor.commit();
    }

    public static String getCurrentPassword(Context ctx){
        return getSharedPreferences(ctx).getString(PASSWORD, "");
    }

    public static void setPushToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PUSH_TOKEN, token);
        editor.commit();
    }

    public static String getPushToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PUSH_TOKEN, "");
    }

}
