package com.tsl.baseapp.updatepasswordemail;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.orhanobut.hawk.Hawk;
import com.rey.material.widget.SnackBar;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.feed.FeedActivity;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.model.event.BaseEvent;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.Utils;
import com.tsl.baseapp.utils.viewhelper.ActionProcessButtonChangeText;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import timber.log.Timber;

import static android.R.attr.id;
import static android.R.id.message;

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

    private final String DEEPLINK_FORGOT_PASSWORD = "forgot-password";
    private final String DEEPLINK_CHANGE_EMAIL_CONFIRM = "change-email-confirm";
    private final String DEEPLINK_CHANGE_EMAIL_VERIFY = "change-email-verify";
    private final String DEEPLINK_CONFIRM_EMAIL = "confirm-email";

    private MaterialDialog mChangingEmailDialog;
    private MaterialDialog mConfirmEmailDialog;

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
    }

    @Override
    public UpdatePasswordAndEmailPresenter createPresenter() {
        return updatePasswordAndEmailComponent.presenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDeepLinks();

        // find out if activity is update email or change password
        String viewType = getArguments().getString(TYPE);
        if (viewType != null){
            if (viewType.equals(CHANGE_PASSWORD)) {
                // inflate change password
                showChangePasswordForm();
            } else {
                // inflate update email
                showUpdateEmailForm();
            }
        }
    }

    @Override
    public void showChangePasswordForm() {
        vs.setShowChangePasswordForm();

        String changePassword = mContext.getString(R.string.change_password);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(changePassword);
        mNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        mConfirmNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        mCurrentPassword.setTransformationMethod(new PasswordTransformationMethod());
        mConfirmNewPassword.setOnEditorActionListener(Utils.closeKeyboardOnEnter(mContext));

        mUpdateEmailForm.setVisibility(View.GONE);
        mChangePasswordForm.setVisibility(View.VISIBLE);
        mSubmitButton.setVisibility(View.VISIBLE);
        mSubmitButton.setNormalText(changePassword);
        setEnabled(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mSubmitButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mSubmitButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword(mCurrentPassword.getText().toString(), mNewPassword.getText().toString(), mConfirmNewPassword.getText().toString());
            }
        }).build();
    }

    @Override
    public void showUpdateEmailForm() {
        vs.setUpdateEmailForm();

        String updateEmail = mContext.getString(R.string.update_email);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(updateEmail);
        mChangePasswordForm.setVisibility(View.GONE);
        mUpdateEmailForm.setVisibility(View.VISIBLE);
        mSubmitButton.setVisibility(View.VISIBLE);
        mSubmitButton.setNormalText(updateEmail);
        setEnabled(true);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mChangeEmailInput.setOnEditorActionListener(Utils.closeKeyboardOnEnter(mContext));
        mSubmitButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mSubmitButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmail(mChangeEmailInput.getText().toString());
            }
        }).build();
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
    public void showChangePasswordSuccess(boolean reset) {
        vs.setShowChangePasswordSuccess();
        Utils.makeToast(mContext, R.string.change_password_success).show();
        mSubmitButton.setProgress(100);
        setEnabled(true);
        if (reset){
            Utils.startActivity(getActivity(), LoginActivity.class, true);
        }
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
    public void showChangingEmailLoading(String body) {
        mChangingEmailDialog.setContent(body);
        mChangingEmailDialog.show();
    }

    @Override
    public void showConfirmEmailSuccess(String message) {
        mChangingEmailDialog.dismiss();
        new MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.update_email))
                .content(message)
                .cancelable(false)
                .show();
    }

    @Override
    public void showVerifyEmailSuccess(String message) {
        mChangingEmailDialog.dismiss();
        new MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.update_email))
                .content(message)
                .cancelable(false)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Branch.getInstance(mContext).logout();
                        Utils.startActivity(getActivity(), FeedActivity.class, true);
                        getActivity().finish();
                    }
                })
                .show();
    }

    @Override
    public void showChangingEmailFailed(String error) {
        mChangingEmailDialog.dismiss();
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingUserHasVerifiedEmail() {
        mConfirmEmailDialog.show();
    }

    @Override
    public void showUserHasVerifiedEmail() {
        mConfirmEmailDialog.dismiss();
        new MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.update_email))
                .content(mContext.getString(R.string.user_has_verified_email))
                .cancelable(false)
                .positiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Utils.startActivity(getActivity(), FeedActivity.class, true);
                        getActivity().finish();
                    }
                })
                .show();
    }

    @Override
    public void showErrorUserHasVerifiedEmail(String error) {
        mConfirmEmailDialog.dismiss();
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
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
        boolean valid = presenter.validateEmail(mChangeEmailInput, mContext);

        if (!valid) return;

        User user = new User();
        user.changeEmail(email);
        presenter.updateEmail(Constants.getToken(), user, mContext);
    }

    private void changePassword(String currentPass, String passNew, String passNewConfirm) {
        boolean valid = presenter.validatePasswords(mNewPassword, mConfirmNewPassword, mContext);

        if (!valid) return;

        User user = new User();
        user.changePassword(currentPass, passNew);
        presenter.changePassword(Constants.getToken(), user, mContext);
    }

    private void changePasswordFromForgotPassword(String token, String passNew) {
        boolean valid = presenter.validatePasswords(mNewPassword, mConfirmNewPassword, mContext);

        if (!valid) return;

        User user = new User();
        user.changePasswordFromForgotPassword(token, passNew);
        presenter.forgotPasswordReset(user);
    }

    private void showErrorSnackbar(String error){
        SnackBar.make(mContext)
                .applyStyle(R.style.SnackBarSingleLine)
                .text(error)
                .singleLine(true)
                .duration(5000)
                .show(getActivity());
    }


    private void getDeepLinks() {
        Branch branch = Branch.getInstance();

        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null && branchUniversalObject != null) {
                    JSONObject branchObject = branchUniversalObject.convertToJson();
                    try {
                        String type = branchObject.getString("type");
                        final String token = branchObject.getString("token");
                        if (type.equals(DEEPLINK_CHANGE_EMAIL_CONFIRM)){
                            mChangingEmailDialog = new MaterialDialog.Builder(mContext)
                                    .title(mContext.getString(R.string.update_email))
                                    .progress(true, 0)
                                    .build();

                            int id = branchObject.getInt("user");
                            User user = new User();
                            user.setToken(token);
                            presenter.confirmEmail(id, user, mContext);
                        }
                        if (type.equals(DEEPLINK_CHANGE_EMAIL_VERIFY)){
                            int id = branchObject.getInt("user");
                            User user = new User();
                            user.setToken(token);
                            presenter.verifyEmail(id, user, mContext);
                        }
                        if (type.equals(DEEPLINK_CONFIRM_EMAIL)){
                            mConfirmEmailDialog = new MaterialDialog.Builder(mContext)
                                    .title(mContext.getString(R.string.user_confirming_email))
                                    .progress(true, 0)
                                    .build();

                            int id = branchObject.getInt("user");
                            User user = new User();
                            user.setToken(token);
                            presenter.confirmUsersEmail(id, user);
                        }
                        if (type.equals(DEEPLINK_FORGOT_PASSWORD)){
                            // remove current password field
                            mCurrentPassword.setVisibility(View.GONE);
                            // inflate change password
                            showChangePasswordForm();
                            mSubmitButton.setMode(ActionProcessButton.Mode.ENDLESS);
                            mSubmitButton.setOnClickNormalState(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    changePasswordFromForgotPassword(token, mConfirmNewPassword.getText().toString());
                                }
                            }).build();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_finding_user), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        }, getActivity().getIntent().getData(), getActivity());
    }

}