package com.tsl.baseapp.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.Utils;

/**
 * Created by kevinlavi on 5/4/16.
 */
public class LogOutDialog {
    public void logout(final Context mContext){
        new MaterialDialog.Builder(mContext)
                .title(R.string.log_out)
                .content(R.string.are_you_sure)
                .positiveText(R.string.yes).onPositive(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        Constants.makeToast(mContext, R.string.logged_out).show();
                        if (isFacebookLoggedIn()) {
                            LoginManager.getInstance().logOut();
                        }
                        Hawk.clear();
                        Activity activity = (Activity) mContext;
                        Utils.startActivityWithoutTransition(activity, LoginActivity.class, true);
                        activity.finish();
                    }
                })
                .negativeText(R.string.no).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                    }
                })
                .show();
    }

    public boolean isFacebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
