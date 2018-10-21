package com.tsl.creditcircle.utils;

import android.view.View;

/**
 * Created by Kevin on 12/8/15.
 */
public abstract class DoubleClickListener implements View.OnClickListener {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

    long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
            onDoubleClick(v);
        }
        lastClickTime = clickTime;
    }
    public abstract void onDoubleClick(View v);
}