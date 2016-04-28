package com.tsl.baseapp.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.login.LoginFragment;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new com.tsl.baseapp.login.LoginFragment())
                    .commit();
        }
    }
}
