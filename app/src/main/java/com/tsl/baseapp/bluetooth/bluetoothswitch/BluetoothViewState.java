package com.tsl.baseapp.bluetooth.bluetoothswitch;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class BluetoothViewState implements ViewState<BluetoothView> {
    final int STATE_SHOW_FORM = 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;

    int state = STATE_SHOW_FORM;

    @Override
    public void apply(BluetoothView view, boolean retained) {
        switch (state) {
            case STATE_SHOW_LOADING:
                view.showLoading();
                break;

            case STATE_SHOW_ERROR:
                view.showError("error");
                break;

            case STATE_SHOW_FORM:
                view.showForm();
                break;

        }
    }

    public void setShowForm() {
        state = STATE_SHOW_FORM;
    }

    public void setShowError() {
        state = STATE_SHOW_ERROR;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }
}
