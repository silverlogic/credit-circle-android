package com.tsl.baseapp.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseAppActivity;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.utils.Constants;

import java.util.concurrent.atomic.AtomicBoolean;

public class FeedActivity extends BaseAppActivity {
    private static AtomicBoolean isRunningTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String token = Hawk.get(Constants.TOKEN);
        if (token == null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new FeedFragment())
                        .commit();
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
