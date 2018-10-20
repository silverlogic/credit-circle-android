package com.tsl.money2020.updatepasswordemail;

import android.content.Intent;
import android.os.Bundle;

import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseAppActivity;


public class UpdatePasswordAndEmailActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Intent intent = getIntent();
        String type = intent.getStringExtra(UpdatePasswordAndEmailFragment.TYPE);

        Bundle bundle = new Bundle();
        bundle.putString(UpdatePasswordAndEmailFragment.TYPE, type);

        if (savedInstanceState == null) {
            UpdatePasswordAndEmailFragment fragment = new UpdatePasswordAndEmailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}
