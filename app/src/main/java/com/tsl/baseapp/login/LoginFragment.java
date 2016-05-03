package com.tsl.baseapp.login;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cesarferreira.painlessprefs.PainlessPrefs;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.tsl.baseapp.BaseApplication;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.Objects.user.AuthCredentials;
import com.tsl.baseapp.model.Utilities.Constants;
import com.tsl.baseapp.model.Utilities.KeyboardUtils;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

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
    @Bind(R.id.loginForm)
    ViewGroup mLoginForm;

    private LoginComponent loginComponent;

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
    }

    @Override
    public ViewState createViewState() {
        return new LoginViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showLoginForm();
    }

    @Override
    public LoginPresenter createPresenter() {
        return loginComponent.presenter();
    }

    @Override
    public void showLoginForm() {
        LoginViewState vs = (LoginViewState) viewState;
        vs.setShowLoginForm();
        changeFbButton();
        setFormEnabled(true);
        mLoginButton.setProgress(0);
    }

    @Override
    public void showError() {
        LoginViewState vs = (LoginViewState) viewState;
        vs.setShowError();

        setFormEnabled(true);
        mLoginButton.setProgress(0);

        Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        LoginViewState vs = (LoginViewState) viewState;
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
        Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_LONG).show();
        //getActivity().finish();
    }
    @Subscribe
    public void onEvent(LoginSuccessfulEvent event){
        PainlessPrefs.getInstance(getActivity().getApplicationContext()).save(Constants.TOKEN, event.getToken());
    }

    public boolean validate() {
        boolean valid = true;

        String email = mInputEmail.getText().toString();
        String password = mInputPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mInputEmail.setError(getString(R.string.valid_email_error));
            valid = false;
        } else {
            mInputEmail.setError(null);
        }

        if (password.isEmpty()) {
            mInputPassword.setError(getString(R.string.enter_password));
            valid = false;
        } else {
            mInputPassword.setError(null);
        }

        return valid;
    }

    @Override
    protected void injectDependencies() {
        loginComponent = DaggerLoginComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    private void login(){
        String username = mInputEmail.getText().toString();
        String pass = mInputPassword.getText().toString();

        if (!validate()) {
            return;
        }

        // Hide keyboard
        if (!KeyboardUtils.hideKeyboard(mInputEmail)) {
            KeyboardUtils.hideKeyboard(mInputPassword);
        }

        // Start login
        presenter.doLogin(new AuthCredentials(username, pass));
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
