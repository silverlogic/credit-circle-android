package com.tsl.baseapp.bluetooth.devicescan;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tsl.baseapp.R;
import com.tsl.baseapp.bluetooth.bluetoothswitch.BluetoothActivity;
import com.tsl.baseapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat;
import no.nordicsemi.android.support.v18.scanner.ScanCallback;
import no.nordicsemi.android.support.v18.scanner.ScanFilter;
import no.nordicsemi.android.support.v18.scanner.ScanResult;
import no.nordicsemi.android.support.v18.scanner.ScanSettings;
import timber.log.Timber;

public class ScanFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_PERMISSION_LOCATION = 1;
    private static final long SCAN_TIMEOUT_MS = 10_000;

    private final BluetoothLeScannerCompat mScanner = BluetoothLeScannerCompat.getScanner();
    private final Handler mStopScanHandler = new Handler();
    private final Runnable mStopScanRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(mContext, "No devices found", Toast.LENGTH_SHORT).show();
            stopLeScan();
        }
    };

    private boolean mScanning;
    private Context mContext;

    public ScanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_scan, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.scan_indeterminate_progress);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                startLeScan();
                break;
            case R.id.menu_stop:
                stopLeScan();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private final ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // We scan with report delay > 0. This will never be called.
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            if (!results.isEmpty()) {
                Timber.d("RESULTS SIZE = " + results.size());
                ScanResult result = results.get(0);
                startInteractActivity(result.getDevice());
                // Device detected, we can automatically connect to it and stop the scan
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            // Scan error
            Timber.w("Scan failed = " + errorCode);
            stopLeScan();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(mContext, "You must turn Bluetooth on, to use this app", Toast.LENGTH_LONG).show();
            getActivity().finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.i(" Location Permission accepted");
        } else {
            Toast.makeText(mContext, "You must grant the location permission.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareForScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLeScan();
    }

    private void prepareForScan() {
        if (isBleSupported()) {
            // Ensures Bluetooth is enabled on the device
            BluetoothManager btManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter btAdapter = btManager.getAdapter();
            if (btAdapter.isEnabled()) {
                // Prompt for runtime permission
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startLeScan();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
                }
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            Toast.makeText(mContext, "BLE is not supported on this device", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private boolean isBleSupported() {
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    private void startLeScan() {
        mScanning = true;

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(1000)
                .build();
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(Constants.DEVICE_UUID)).build());
        mScanner.startScan(filters, settings, mScanCallback);

        // Stops scanning after a pre-defined scan period.
        mStopScanHandler.postDelayed(mStopScanRunnable, SCAN_TIMEOUT_MS);

        getActivity().invalidateOptionsMenu();
    }

    private void stopLeScan() {
        if (mScanning) {
            mScanning = false;

            mScanner.stopScan(mScanCallback);
            mStopScanHandler.removeCallbacks(mStopScanRunnable);

            getActivity().invalidateOptionsMenu();
        }
    }

    private void startInteractActivity(BluetoothDevice device) {
        String deviceAddress = device.getAddress();
        Timber.d("DEVICE CHECK - " + deviceAddress);
        Intent intent = new Intent(getActivity(), BluetoothActivity.class);
        intent.putExtra(BluetoothActivity.EXTRA_DEVICE_ADDRESS, device.getAddress());
        startActivity(intent);
        getActivity().finish();
    }

}
