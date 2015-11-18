package com.tsl.baseapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tsl.baseapp.Controller.CurrentUserController;
import com.tsl.baseapp.Controller.SignUpLoginController;
import com.tsl.baseapp.Model.Objects.User;
import com.tsl.baseapp.Model.Utilities.Constants;
import com.tsl.baseapp.Model.Utilities.SaveSharedPreference;
import com.tsl.baseapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements SignUpLoginController.SignUpLoginCallBackListener, CurrentUserController.CurrentUserControllerCallBackListener{

    private static final String TAG = "SignupActivity";
    private SignUpLoginController mSignUpLoginController;
    private CurrentUserController mCurrentUserController;
    private ProgressDialog progressDialog;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_password_confirm)
    EditText _password_confirm;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    @Bind(R.id.input_first_name)
    EditText mInputFirstName;
    @Bind(R.id.input_last_name)
    EditText mInputLastName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mSignUpLoginController = new SignUpLoginController(this, this);
        mCurrentUserController = new CurrentUserController(this, this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String first_name = mInputFirstName.getText().toString();
        String last_name = mInputLastName.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirm_pass= _password_confirm.getText().toString();

        User mUser = new User(email, password, confirm_pass, first_name, last_name);
        mSignUpLoginController.signUp(mUser);

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String first_name = mInputFirstName.getText().toString();
        String last_name = mInputLastName.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String confirm_pass= _password_confirm.getText().toString();

        if (first_name.isEmpty()) {
            mInputFirstName.setError("Enter a first name");
            valid = false;
        } else {
            mInputFirstName.setError(null);
        }

        if (last_name.isEmpty()) {
            mInputLastName.setError("Enter a last name");
            valid = false;
        } else {
            mInputLastName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Enter Password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (confirm_pass.isEmpty() || !confirm_pass.equals(password)) {
            _password_confirm.setError("Passwords must match");
            valid = false;
        } else {
            _password_confirm.setError(null);
        }

        return valid;
    }

    @Override
    public void onLoginComplete(String token) {
        token = token.replace("\"", "");
        SaveSharedPreference.setToken(this, token);
        mCurrentUserController.getCurrentUser(Constants.getToken(this));
    }

    @Override
    public void onLoginFailure() {

    }

    @Override
    public void onSignUpComplete() {
    }

    @Override
    public void onSignUpFailed() {
        progressDialog.dismiss();
        onSignupFailed();

    }

    @Override
    public void onPassChanged() {

    }

    @Override
    public void onPassFailed() {

    }

    @Override
    public void onFacebookLoggedIn(String token) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onUserComplete(User user) {
        progressDialog.dismiss();
        onSignupSuccess();
    }

    @Override
    public void onUserFailure() {
        progressDialog.dismiss();
        onSignUpFailed();
    }
}
