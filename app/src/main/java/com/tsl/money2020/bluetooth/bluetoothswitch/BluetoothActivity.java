package com.tsl.money2020.bluetooth.bluetoothswitch;

import android.os.Bundle;

import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseAppActivity;

public class BluetoothActivity extends BaseAppActivity {

    public static final String EXTRA_DEVICE_ADDRESS = "mAddress";

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
