package com.tsl.baseapp.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Kevin Lavi on 8/9/16.
 */

public class Utils {

    private Utils(){};

    public void startActivityWithoutTransition(Activity currentActivity, Activity newActivity){
        Intent intent = new Intent(currentActivity, newActivity.getClass());
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(0, 0);
    }
}
