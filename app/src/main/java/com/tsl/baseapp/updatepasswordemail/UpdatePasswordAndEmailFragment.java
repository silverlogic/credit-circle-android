package com.tsl.baseapp.updatepasswordemail;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.rey.material.widget.SnackBar;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.event.BaseEvent;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.Utils;
import com.tsl.baseapp.utils.viewhelper.ActionProcessButtonChangeText;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;

public class UpdatePasswordAndEmailFragment extends BaseViewStateFragment<UpdatePasswordAndEmailView, UpdatePasswordAndEmailPresenter> implements UpdatePasswordAndEmailView {

    @Bind(R.id.current_password)
    EditText mCurrentPassword;
    @Bind(R.id.new_password)
    EditText mNewPassword;
    @Bind(R.id.confirm_new_password)
    EditText mConfirmNewPassword;
    @Bind(R.id.change_password_form)
    LinearLayout mChangePasswordForm;
    @Bind(R.id.change_email_input)
    EditText mChangeEmailInput;
    @Bind(R.id.update_email_form)
    LinearLayout mUpdateEmailForm;
    @Bind(R.id.submit_button)
    ActionProcessButtonChangeText mSubmitButton;

    public static final String TYPE = "type";
    public static final String CHANGE_PASSWORD = "changePassword";
    public static final String UPDATE_EMAIL = "updateEmail";

    private Context mContext;
    private UpdatePasswordAndEmailViewState vs;
    private UpdatePasswordAndEmailComponent updatePasswordAndEmailComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_update_password_email;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
    }

    @Override
    public ViewState createViewState() {
        return new UpdatePasswordAndEmailViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (UpdatePasswordAndEmailViewState) viewState;
        // find out if activity is update email or change password
        Intent intent = getActivity().getIntent();
        String viewType = intent.getStringExtra(TYPE);
        if (viewType.equals(CHANGE_PASSWORD)) {
            // inflate change password
            showChangePasswordForm();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mSubmitButton.setMode(ActionProcessButton.Mode.ENDLESS);
            mSubmitButton.setOnClickNormalState(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassword(mCurrentPassword.getText().toString(), mNewPassword.getText().toString(), mConfirmNewPassword.getText().toString());
                }
            }).build();
        } else {
            // inflate update email
            showUpdateEmailForm();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mSubmitButton.setMode(ActionProcessButton.Mode.ENDLESS);
            mSubmitButton.setOnClickNormalState(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeEmail(mChangeEmailInput.getText().toString());
                }
            }).build();
        }
    }

    @Override
    public UpdatePasswordAndEmailPresenter createPresenter() {
        return updatePasswordAndEmailComponent.presenter();
    }

    @Override
    public void showChangePasswordForm() {
        vs.setShowChangePasswordForm();
        String changePassword = mContext.getString(R.string.change_password);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(changePassword);
        mNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        mConfirmNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        mCurrentPassword.setTransformationMethod(new PasswordTransformationMethod());


        mUpdateEmailForm.setVisibility(View.GONE);
        mChangePasswordForm.setVisibility(View.VISIBLE);
        mSubmitButton.setNormalText(changePassword);
        setEnabled(true);
    }

    @Override
    public void showUpdateEmailForm() {
        vs.setUpdateEmailForm();
        String updateEmail = mContext.getString(R.string.update_email);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(updateEmail);
        mChangePasswordForm.setVisibility(View.GONE);
        mUpdateEmailForm.setVisibility(View.VISIBLE);
        mSubmitButton.setNormalText(updateEmail);
        setEnabled(true);
    }

    @Override
    public void showError(String error) {
        vs.setShowError(error);
        mSubmitButton.setProgress(0);
        showErrorSnackbar(error);
        setEnabled(true);
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();
        mSubmitButton.setProgress(30);
        setEnabled(false);
    }

    @Override
    public void showChangePasswordSuccess() {
        vs.setShowChangePasswordSuccess();
        Utils.makeToast(mContext, R.string.change_password_success).show();
        mSubmitButton.setProgress(100);
        setEnabled(true);
        getActivity().finish();
    }

    @Override
    public void showUpdateEmailSuccess() {
        vs.setUpdateEmailSuccess();
        Utils.makeToast(mContext, R.string.email_update_sent).show();
        mSubmitButton.setProgress(100);
        setEnabled(true);
        getActivity().finish();
    }

    @Override
    protected void injectDependencies() {
        updatePasswordAndEmailComponent = DaggerUpdatePasswordAndEmailComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(BaseEvent event) {
        // Do stuff with Eventbus event
    }

    private void setEnabled(boolean enabled) {
        mSubmitButton.setEnabled(enabled);
        mNewPassword.setEnabled(enabled);
        mConfirmNewPassword.setEnabled(enabled);
        mChangeEmailInput.setEnabled(enabled);
    }

    public void changeEmail(String email) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mChangeEmailInput.setError(mContext.getString(R.string.valid_email_error));
        } else {
            User user = new User();
            user.changeEmail(email);
            presenter.updateEmail(Constants.getToken(), user, mContext);
        }
    }

    private void changePassword(String currentPass, String passNew, String passNewConfirm) {
        if (passNew.isEmpty()) {
            mNewPassword.setError(mContext.getString(R.string.field_cannot_be_empty));
        } else if (passNewConfirm.isEmpty()) {
            mConfirmNewPassword.setError(mContext.getString(R.string.field_cannot_be_empty));
        } else if (!passNew.equals(passNewConfirm)) {
            mConfirmNewPassword.setError(mContext.getString(R.string.new_password_error));
        } else {
            User user = new User();
            user.changePassword(currentPass, passNew);
            presenter.changePassword(Constants.getToken(), user, mContext);
        }
    }

    private void showErrorSnackbar(String error){
        SnackBar.make(mContext)
                .applyStyle(R.style.SnackBarSingleLine)
                .text(error)
                .singleLine(true)
                .duration(5000)
                .show(getActivity());
    }
}
