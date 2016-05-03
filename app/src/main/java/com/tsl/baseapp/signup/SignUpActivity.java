package com.tsl.baseapp.signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseActivity;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new com.tsl.baseapp.signup.SignUpFragment())
                    .commit();
        }
    }
}
