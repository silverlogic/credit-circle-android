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
                        Hawk.remove(Constants.TOKEN);
                        Hawk.remove(Constants.USER);
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        logoutIntent(intent, mContext);
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

    private void logoutIntent(Intent intent, Context context){
        Activity activity = (Activity) context;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }
}
