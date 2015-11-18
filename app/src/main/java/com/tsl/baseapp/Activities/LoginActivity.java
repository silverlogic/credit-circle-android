package com.tsl.baseapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tsl.baseapp.Controller.CurrentUserController;
import com.tsl.baseapp.Controller.SignUpLoginController;
import com.tsl.baseapp.Model.Objects.User;
import com.tsl.baseapp.Model.Utilities.Constants;
import com.tsl.baseapp.Model.Utilities.SaveSharedPreference;
import com.tsl.baseapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements SignUpLoginController.SignUpLoginCallBackListener,
        CurrentUserController.CurrentUserControllerCallBackListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    SignUpLoginController mSignUpLoginController;
    CurrentUserController mCurrentUserController;
    private ProgressDialog progressDialog;
    private CallbackManager mCallbackManager;


    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    @Bind(R.id.facebook_login_button)
    LoginButton mFacebookLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        changeFbButton();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //mLinkedinLogin.setTransformationMethod(null);
        _loginButton.setTransformationMethod(null);

        mCallbackManager = CallbackManager.Factory.create();
        mSignUpLoginController = new SignUpLoginController(this, this);
        mCurrentUserController = new CurrentUserController(this, this);


        mFacebookLoginButton.setReadPermissions("public_profile", "email");

        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                User mUser = new User(loginResult.getAccessToken().getToken(), "facebook");
                mSignUpLoginController.facebookLogin(mUser);
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOKLOGIN:: ", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOKERROR:: ", error.toString());
            }
        });

        _emailText.setHintTextColor(getResources().getColor(R.color.white));

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(0, 0);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        User mUser = new User();
        mUser.setEmail(email);
        mUser.setPassword(password);
        mSignUpLoginController.login(mUser);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Enter password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onLoginComplete(String token) {
        token = token.replace("\"", "");
        SaveSharedPreference.setToken(this, token);
        mCurrentUserController.getCurrentUser(Constants.getToken(this)); //get token
    }

    @Override
    public void onLoginFailure() {
        progressDialog.dismiss();
        onLoginFailed();

    }

    @Override
    public void onSignUpComplete() {
    }

    @Override
    public void onSignUpFailed() {

    }

    @Override
    public void onPassChanged() {

    }

    @Override
    public void onPassFailed() {

    }

    @Override
    public void onFacebookLoggedIn(String token) {
        token = token.replace("\"", "");
        SaveSharedPreference.setToken(this, token);
        mCurrentUserController.getCurrentFacebookUser(Constants.getToken(this));
    }

    @Override
    public void onUserComplete(User user) {
        SaveSharedPreference.setCurrentUser(user, this);
        progressDialog.dismiss();
        onLoginSuccess();

    }

    @Override
    public void onUserFailure() {
        progressDialog.dismiss();
        onLoginFailed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
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