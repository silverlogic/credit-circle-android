package com.tsl.creditcircle.utils;

/**
 * Created by kevinlavi on 4/26/16.
 */
import android.view.View;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    /**
     * Hides the soft keyboard from screen
     *
     * @param view Usually the EditText, but in dynamically  layouts you should pass the layout
     * instead of the EditText
     * @return true, if keyboard has been hidden, otherwise false (i.e. the keyboard was not displayed
     * on the screen or no Softkeyboard because device has hardware keyboard)
     */
    public static boolean hideKeyboard(View view) {

        if (view == null) {
            throw new NullPointerException("View is null!");
        }

        try {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm == null) {
                return false;
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}