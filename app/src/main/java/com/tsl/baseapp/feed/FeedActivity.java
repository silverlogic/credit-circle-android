package com.tsl.baseapp.feed;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(FeedActivity.this, SettingsActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }

        if (id == R.id.action_profile) {
            User user = Hawk.get(Constants.USER);
            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra(UserDetailsFragment.USER, user);
            intent.putExtra(UserDetailsFragment.IS_CURRENT_USER, true);
            startActivity(intent);
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
