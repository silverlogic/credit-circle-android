package com.tsl.creditcircle.utils;

/**
 * Created by kevinlavi on 2/24/16.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tsl.creditcircle.R;


public class ConnectivityHelper {

    public static void notifyUserAboutNoInternetConnectivity(Context context) {
        notifyUserAboutGenericError(context, R.string.no_internet);
    }

    public static void notifyUserAboutAPIError(Context context) {
        notifyUserAboutGenericError(context, R.string.api_error);
    }

    public static void notifyUserAboutGenericError(Context context, int textId) {
        new AlertDialog.Builder(context).setTitle(textId)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}