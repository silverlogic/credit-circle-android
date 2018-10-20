package com.tsl.money2020.signup;

import android.os.Bundle;

import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseAppActivity;

public class SignUpActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new com.tsl.money2020.signup.SignUpFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
