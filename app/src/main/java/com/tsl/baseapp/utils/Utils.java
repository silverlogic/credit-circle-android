package com.tsl.baseapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Kevin Lavi on 8/9/16.
 */

public class Utils {

    private Utils(){};

    public static final void startActivity(Activity currentActivity, Class newActivity, boolean clearStack){
        Intent intent = new Intent(currentActivity, newActivity);
        if (clearStack){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(0, 0);
    }

    public static final void startActivityWithoutTransition(Activity currentActivity, Class newActivity, boolean clearStack){
        Intent intent = new Intent(currentActivity, newActivity);
        if (clearStack){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        currentActivity.startActivity(intent);
    }

    public static final Toast makeToast(Context ctx, int msg){
        return  Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
    }

    public static final String parseDate(String originalDate){
        DateTimeFormatter parser = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTime datetime = parser.parseDateTime(originalDate);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM d, yyyy");
        String date = formatter.print(datetime);
        return date;
    }

    public static final TextView.OnEditorActionListener closeKeyboardOnEnter(final Context context){
        // used to close keyboard after pressing enter on keyboard
        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

                    in.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;

                }
                return false;
            }
        };
        return listener;
    }
}
