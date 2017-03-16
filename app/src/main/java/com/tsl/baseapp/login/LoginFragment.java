package com.tsl.baseapp.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.feed.FeedActivity;
import com.tsl.baseapp.forgotpassword.ForgotPasswordActivity;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.settings.SettingsActivity;
import com.tsl.baseapp.signup.SignUpActivity;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.KeyboardUtils;
import com.tsl.baseapp.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 */
public class LoginFragment extends BaseViewStateFragment<LoginView, LoginPresenter> implements LoginView {

    @Bind(R.id.input_email)
    EditText mInputEmail;
    @Bind(R.id.input_password)
    EditText mInputPassword;
    @Bind(R.id.loginButton)
    ActionProcessButton mLoginButton;
    @Bind(R.id.facebook_login_button)
    LoginButton mFacebookLoginButton;
    @Bind(R.id.link_signup)
    TextView mSignupLink;
    @Bind(R.id.link_forgot_password)
    TextView mLinkForgotPassword;
    @Bind(R.id.loginForm)
    ViewGroup mLoginForm;

    private LoginComponent loginComponent;
    private LoginViewState vs;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mLoginButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        }).build();
        mContext = getContext();
    }

    @Override
    public ViewState createViewState() {
        return new LoginViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (LoginViewState) viewState;
        showLoginForm();
    }

    @Override
    public LoginPresenter createPresenter() {
        return loginComponent.presenter();
    }

    @Override
    public void showLoginForm() {
        vs.setShowLoginForm();
        changeFbButton();
        // set password form to hide inputs
        mInputPassword.setTransformationMethod(new PasswordTransformationMethod());
        setFormEnabled(true);
        mLoginButton.setProgress(0);
    }

    @Override
    public void showError(String error) {
        vs.setShowError();

        setFormEnabled(true);
        mLoginButton.setProgress(0);

        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();

        setFormEnabled(false);
        // any progress between 0 - 100 shows animation
        mLoginButton.setProgress(30);
    }

    private void setFormEnabled(boolean enabled) {
        mInputEmail.setEnabled(enabled);
        mInputPassword.setEnabled(enabled);
        mLoginButton.setEnabled(enabled);
        mFacebookLoginButton.setEnabled(enabled);
        mSignupLink.setEnabled(enabled);
    }

    @Override
    public void loginSuccessful() {
        mLoginButton.setProgress(100); // We are done
        Utils.startActivity(getActivity(), FeedActivity.class, true);
        getActivity().finish();
    }

    @Subscribe
    public void onEvent(LoginSuccessfulEvent event) {
    }

    @Override
    protected void injectDependencies() {
        loginComponent = DaggerLoginComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    private void login() {
        String username = mInputEmail.getText().toString();
        String pass = mInputPassword.getText().toString();
        LoginValidation validation = new LoginValidation();
        boolean valid = validation.validate(mInputEmail, mInputPassword, mContext);

        if (!valid) {
            return;
        }

        // Hide keyboard
        if (!KeyboardUtils.hideKeyboard(mInputEmail)) {
            KeyboardUtils.hideKeyboard(mInputPassword);
        }

        User user = new User();
        user.login(username, pass);

        // Start login
        presenter.doLogin(user, mContext);
    }

    @OnClick(R.id.link_signup)
    public void signUpActivity() {
        Utils.startActivity(getActivity(), SignUpActivity.class, false);
    }

    @OnClick(R.id.link_forgot_password)
    public void forgotPasswordActivity() {
        Utils.startActivity(getActivity(), ForgotPasswordActivity.class, false);
    }

    private void changeFbButton() {
        float fbIconScale = 1.45F;
        Drawable drawable = this.getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * fbIconScale),
                (int) (drawable.getIntrinsicHeight() * fbIconScale));
        mFacebookLoginButton.setCompoundDrawables(drawable, null, null, null);
        mFacebookLoginButton.setCompoundDrawablePadding(this.getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        mFacebookLoginButton.setPadding(
                mFacebookLoginButton.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                mFacebookLoginButton.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                mFacebookLoginButton.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));
    }
}
