package com.tsl.money2020.settings;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.hawk.Hawk;
import com.tsl.money2020.R;
import com.tsl.money2020.login.LoginActivity;
import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.utils.Utils;
import com.tsl.money2020.utils.Writer;

import io.realm.Realm;

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
                        Hawk.clear();
                        Activity activity = (Activity) mContext;
                        Utils.startActivityWithoutTransition(activity, LoginActivity.class, true);
                        activity.finish();
                        Writer.execute(new Writer.Task() {
                            @Override
                            public void run(Realm realm) {
                                realm.deleteAll();
                            }
                        });
                    }
                })
                .negativeText(R.string.no).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                    }
                })
                .show();
    }
}
