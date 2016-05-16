package com.tsl.baseapp.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.tsl.baseapp.R;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.objects.user.ChangePasswordCredentials;
import com.tsl.baseapp.utils.Constants;

import butterknife.Bind;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by kevinlavi on 5/4/16.
 */
public class ChangePasswordDialog {

    @Bind(R.id.old_password)
    EditText oldPass;
    @Bind(R.id.new_password)
    EditText newPass;
    @Bind(R.id.confirm_new_password)
    EditText confirmNewPass;

    private String passNewConfirm;
    private String pass;
    private String passNew;
    private ProgressDialog progressDialog;

    public void setDialog(final Context mContext, final String token) {
        boolean wrapInScrollView = true;
        final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(R.string.change_password)
                .customView(R.layout.change_password_view, wrapInScrollView)
                .positiveText(R.string.submit)
                .build();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPass.setEnabled(s.toString().trim().length() > 0);
                newPass.setEnabled(s.toString().trim().length() > 0);
                confirmNewPass.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //noinspection ConstantConditions
        oldPass = (EditText) dialog.getCustomView().findViewById(R.id.old_password);
        newPass = (EditText) dialog.getCustomView().findViewById(R.id.new_password);
        confirmNewPass = (EditText) dialog.getCustomView().findViewById(R.id.confirm_new_password);
        dialog.show();


        MDButton b = dialog.getActionButton(DialogAction.POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = oldPass.getText().toString();
                passNew = newPass.getText().toString();
                passNewConfirm = confirmNewPass.getText().toString();

                if (pass.isEmpty()) {
                    oldPass.setError(mContext.getString(R.string.old_pass_must_match_current));
                } else if (passNew.isEmpty()) {
                    newPass.setError(mContext.getString(R.string.field_cannot_be_empty));
                } else if (passNewConfirm.isEmpty()) {
                    confirmNewPass.setError(mContext.getString(R.string.field_cannot_be_empty));
                } else if (!passNew.equals(passNewConfirm)) {
                    confirmNewPass.setError(mContext.getString(R.string.new_password_error));
                } else {
                    ChangePasswordCredentials credentials = new ChangePasswordCredentials(pass, passNew, passNewConfirm);
                    change(token, credentials, mContext);
                    progressDialog = new ProgressDialog(mContext,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(mContext.getString(R.string.changing_password));
                    progressDialog.show();
                    dialog.dismiss();
                }
            }
        });

    }

    private void change(String token, ChangePasswordCredentials credentials, final Context ctx){
        final BaseApi api = new BaseApiManager().getAppApi();
        Subscription changePasswordSubscriber = api.changePassword(token, credentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                        Constants.makeToast(ctx, R.string.change_password_success).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Timber.d(e.getMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }
}
