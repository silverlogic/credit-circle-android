package com.tsl.money2020.login;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseAppActivity;

public class LoginActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new com.tsl.money2020.login.LoginFragment())
                    .commit();
        }
    }
}
