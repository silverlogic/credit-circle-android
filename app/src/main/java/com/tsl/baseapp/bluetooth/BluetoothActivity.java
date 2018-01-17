package com.tsl.baseapp.bluetooth;

import android.os.Bundle;

import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseAppActivity;

public class BluetoothActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new BluetoothFragment())
                    .commit();
        }
    }
}