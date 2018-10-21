package com.tsl.creditcircle.forgotpassword;

import android.os.Bundle;
import com.tsl.creditcircle.R;
import com.tsl.creditcircle.base.BaseAppActivity;

public class ForgotPasswordActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new ForgotPasswordFragment())
                    .commit();
        }
    }
}
