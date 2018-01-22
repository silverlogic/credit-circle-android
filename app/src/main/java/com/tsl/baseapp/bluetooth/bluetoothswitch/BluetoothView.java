package com.tsl.baseapp.bluetooth.bluetoothswitch;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/6/16.
 */
public interface BluetoothView extends MvpView{
    public void showForm();

    public void showError(String error);

    public void showLoading();
}
