package com.tsl.money2020.baseview;

import android.os.Bundle;

import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseAppActivity;

public class BaseActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new BaseFragment())
                    .commit();
        }
    }
}
