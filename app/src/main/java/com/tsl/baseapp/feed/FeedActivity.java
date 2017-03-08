package com.tsl.baseapp.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseAppActivity;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.userdetails.UserDetailsActivity;
import com.tsl.baseapp.userdetails.UserDetailsFragment;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.settings.SettingsActivity;

public class FeedActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        User user = Hawk.get(Constants.USER);
        if (user == null){
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
