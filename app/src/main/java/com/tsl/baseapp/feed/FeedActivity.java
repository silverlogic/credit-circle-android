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

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FeedActivity extends BaseAppActivity {
    private static AtomicBoolean isRunningTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isTesting()){
            // Realm
            // robolectric can not run with realm. This is why we init in opening activity instead of application
            Realm.init(this);
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(config);
            Realm.getDefaultInstance();

        }

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

    private boolean isTesting() {
        // if testing do not initiate realm
        try {
            Class.forName("com.tsl.baseapp.feed.FeedPresenterTest");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
