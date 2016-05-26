package com.tsl.baseapp.signup;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;
import android.widget.EditText;

import com.tsl.baseapp.R;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class SignUpValidation {

    public boolean validate(EditText inputFirstName, EditText inputLastName, EditText inputEmail, 
                            EditText inputPassword, EditText inputConfirmPassword, Context context) {
        
        boolean valid = true;

        String first_name = inputFirstName.getText().toString();
        String last_name = inputLastName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirm_pass= inputConfirmPassword.getText().toString();
        Context mContext = context;
        Resources r = mContext.getResources();

        if (first_name.isEmpty()) {
            inputFirstName.setError(r.getString(R.string.enter_first_name));
            valid = false;
        } else {
            inputFirstName.setError(null);
        }

        if (last_name.isEmpty()) {
            inputLastName.setError(r.getString(R.string.enter_last_name));
            valid = false;
        } else {
            inputLastName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError(r.getString(R.string.enter_valid_emil));
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

        if (confirm_pass.isEmpty() || !confirm_pass.equals(password)) {
            inputConfirmPassword.setError(r.getString(R.string.passwords_must_match));
            valid = false;
        } else {
            inputConfirmPassword.setError(null);
        }

        return valid;
    }
}