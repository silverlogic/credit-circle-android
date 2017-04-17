package com.tsl.baseapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.feed.FeedActivity;
import com.tsl.baseapp.forgotpassword.ForgotPasswordActivity;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;
import com.tsl.baseapp.model.event.TokenEvent;
import com.tsl.baseapp.model.objects.error.SocialError;
import com.tsl.baseapp.model.objects.user.SocialAuth;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.signup.SignUpActivity;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.KeyboardUtils;
import com.tsl.baseapp.utils.Utils;
import com.tsl.baseapp.utils.Writer;

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
    @Bind(R.id.facebook_loginButton)
    ActionProcessButton mFacebookLoginButton;
    @Bind(R.id.linkedIn_loginButton)
    ActionProcessButton mLinkedInLoginButton;
    @Bind(R.id.twitter_loginButton)
    ActionProcessButton mTwitterLoginButton;
    @Bind(R.id.link_signup)
    TextView mSignupLink;
    @Bind(R.id.link_forgot_password)
    TextView mLinkForgotPassword;
    @Bind(R.id.loginForm)
    ViewGroup mLoginForm;

    private LoginComponent loginComponent;
    private LoginViewState vs;
    private Context mContext;
    private final int FACEBOOK_REQUEST = 1;
    private final int LINKEDIN_REQUEST = 2;
    private final int TWITTER_REQUEST = 3;
    ActionProcessButton mActiveButton;

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
                validate();
            }
        }).build();

        mFacebookLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mFacebookLoginButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFacebook();
            }
        }).build();

        mLinkedInLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mLinkedInLoginButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithLinkedIn();
            }
        }).build();

        mTwitterLoginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mTwitterLoginButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithTwitter();
            }
        }).build();

        mContext = getContext();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mActiveButton != null && mActiveButton == mTwitterLoginButton) {
            mActiveButton.setProgress(0);
            setFormEnabled(true);
        }
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
        // set password form to hide inputs
        mInputPassword.setTransformationMethod(new PasswordTransformationMethod());
        setFormEnabled(true);
        mLoginButton.setProgress(0);
        mFacebookLoginButton.setProgress(0);
        mLinkedInLoginButton.setProgress(0);
        mTwitterLoginButton.setProgress(0);
    }

    @Override
    public void showError(String error) {
        vs.setShowError();
        if (error.equals(SocialError.NO_EMAIL_PROVIDED)){
            // ask user for email
            oAuth1ErrorHandling();
        }
        else {
            setFormEnabled(true);
            mLoginButton.setProgress(0);
            mFacebookLoginButton.setProgress(0);
            mLinkedInLoginButton.setProgress(0);
            mTwitterLoginButton.setProgress(0);

            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();

        setFormEnabled(false);
        // any progress between 0 - 100 shows animation
        mActiveButton.setProgress(30);
    }

    private void setFormEnabled(boolean enabled) {
        mInputEmail.setEnabled(enabled);
        mInputPassword.setEnabled(enabled);
        mLoginButton.setEnabled(enabled);
        mFacebookLoginButton.setEnabled(enabled);
        mSignupLink.setEnabled(enabled);
        mTwitterLoginButton.setEnabled(enabled);
        mLinkedInLoginButton.setEnabled(enabled);
    }

    @Override
    public void loginSuccessful() {
        mActiveButton.setProgress(100); // We are done
        Utils.startActivity(getActivity(), FeedActivity.class, true);
        getActivity().finish();
    }

    @Override
    public void twitterLogin() {
        SocialAuth user = Hawk.get(Constants.TWITTER_USER);
        String url = mContext.getResources().getString(R.string.twitter_login_url, user.getOauthToken(), Constants.REDIRECT_URL);
        Intent intent = OAuth1LoginActivity.getIntent(mContext, url,
                mContext.getString(R.string.login_twitter),
                ContextCompat.getColor(mContext, R.color.twitterColor),
                ContextCompat.getColor(mContext, R.color.twitterColorDark));
        startActivityForResult(intent, TWITTER_REQUEST);
    }
    @Subscribe
    public void onEvent(LoginSuccessfulEvent event) {
        User user = event.getUser();
        // persist user id for fetching from realms
        Hawk.put(Constants.USER_ID, user.getId());
        // persist current user
        User persistedUser = Writer.persist(user);
    }

    @Subscribe
    public void onEvent(TokenEvent event) {
        Hawk.put(Constants.TOKEN, event.getToken());
    }

    @Override
    protected void injectDependencies() {
        loginComponent = DaggerLoginComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    private void validate(){
        boolean valid = presenter.validate(mInputEmail, mInputPassword, mContext);

        if (!valid) return;

        String username = mInputEmail.getText().toString();
        String pass = mInputPassword.getText().toString();

        // Hide keyboard
        if (!KeyboardUtils.hideKeyboard(mInputEmail)) {
            KeyboardUtils.hideKeyboard(mInputPassword);
        }

        User user = new User();
        user.login(username, pass);

        mActiveButton = mLoginButton;

        // Start login
        presenter.doNormalLogin(mContext, user);
    }

    private void loginWithFacebook(){
        mActiveButton = mFacebookLoginButton;
        mFacebookLoginButton.setProgress(0);
        String url = Constants.getOAuth2LoginURLForFacebook(mContext, R.string.facebook_login_url, Constants.FACEBOOK_APP_ID);
        Intent intent = OAuth2LoginActivity.getIntent(mContext, url,
                mContext.getString(R.string.login_facebook),
                ContextCompat.getColor(mContext, R.color.facebookColor),
                ContextCompat.getColor(mContext, R.color.facebookColorDark));
        startActivityForResult(intent, FACEBOOK_REQUEST);
    }

    private void loginWithLinkedIn(){
        mActiveButton = mLinkedInLoginButton;
        mLinkedInLoginButton.setProgress(0);
        String url = Constants.getOAuth2LoginURLForLinkedIn(mContext, R.string.linkedin_login_url, Constants.LINKEDIN_CLIENT_ID);
        Intent intent = OAuth2LoginActivity.getIntent(mContext, url,
                mContext.getString(R.string.login_linkedin),
                ContextCompat.getColor(mContext, R.color.linkedinColor),
                ContextCompat.getColor(mContext, R.color.linkedinColorDark));
        startActivityForResult(intent, LINKEDIN_REQUEST);
    }

    private void loginWithTwitter(){
        mActiveButton = mTwitterLoginButton;
        mTwitterLoginButton.setProgress(0);
        SocialAuth user = new SocialAuth();
        user.setProvider("twitter");
        user.setRedirectURL(Constants.REDIRECT_URL);
        presenter.doTwitterLogin(mContext, user);
    }

    private void oAuth1ErrorHandling(){
        new MaterialDialog.Builder(getActivity())
                .title(R.string.twitter_no_email_title)
                .content(R.string.twitter_no_email_body)
                .autoDismiss(false)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .canceledOnTouchOutside(false)
                .negativeText(mContext.getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        setFormEnabled(true);
                        mLoginButton.setProgress(0);
                        mFacebookLoginButton.setProgress(0);
                        mLinkedInLoginButton.setProgress(0);
                        mTwitterLoginButton.setProgress(0);

                        Toast.makeText(getActivity(), mContext.getString(R.string.error_logging_in), Toast.LENGTH_LONG).show();
                    }
                })
                .input(R.string.email, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        if (validateEmail(input.toString())){
                            dialog.dismiss();
                            SocialAuth auth = Hawk.get(Constants.TWITTER_USER);
                            auth.setEmail(input.toString());
                            presenter.doOAuthLogin(mContext, auth);
                        }
                        else {
                            Toast.makeText(mContext, mContext.getString(R.string.valid_email_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    private boolean validateEmail(String email){
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
        }
        return valid;
    }

    @OnClick(R.id.link_signup)
    public void signUpActivity() {
        Utils.startActivity(getActivity(), SignUpActivity.class, false);
    }

    @OnClick(R.id.link_forgot_password)
    public void forgotPasswordActivity() {
        Utils.startActivity(getActivity(), ForgotPasswordActivity.class, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FACEBOOK_REQUEST && resultCode == Activity.RESULT_OK) {
            String code = data.getStringExtra(OAuth2LoginActivity.AUTH_CODE);
            SocialAuth user = SocialAuth.forFacebook(mContext, code);
            presenter.doOAuthLogin(mContext, user);
        }

        if (requestCode == LINKEDIN_REQUEST && resultCode == Activity.RESULT_OK) {
            String code = data.getStringExtra(OAuth2LoginActivity.AUTH_CODE);
            SocialAuth user = SocialAuth.forLinkedIn(mContext, code);
            presenter.doOAuthLogin(mContext, user);
        }

        if (requestCode == TWITTER_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String verifier = bundle.getString(OAuth1LoginActivity.VERIFIER);
            String token = bundle.getString(OAuth1LoginActivity.TOKEN);
            SocialAuth auth = Hawk.get(Constants.TWITTER_USER);
            auth.setOauthToken(token);
            auth.setOauthTokenVerifier(verifier);
            auth.setProvider("twitter");
            Hawk.put(Constants.TWITTER_USER, auth);
            presenter.doOAuthLogin(mContext, auth);
        }
    }
}