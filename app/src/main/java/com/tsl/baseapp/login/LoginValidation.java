package com.tsl.baseapp.login;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;
import android.widget.EditText;

import com.tsl.baseapp.R;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class LoginValidation {

    public boolean validate(EditText inputEmail, EditText inputPassword, Context context) {
        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        Context mContext = context;
        Resources r = mContext.getResources();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError(r.getString(R.string.valid_email_error));
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty()) {
            inputPassword.setError(r.getString(R.string.enter_password));
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

}
