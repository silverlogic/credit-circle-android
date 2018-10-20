package com.tsl.money2020.bluetooth.bluetoothswitch;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseApplication;
import com.tsl.money2020.base.BaseViewStateFragment;
import com.tsl.money2020.bluetooth.GattClient;
import com.tsl.money2020.model.event.BaseEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

import static com.tsl.money2020.bluetooth.bluetoothswitch.BluetoothActivity.EXTRA_DEVICE_ADDRESS;

public class BluetoothFragment extends BaseViewStateFragment<BluetoothView, BluetoothPresenter> implements BluetoothView {

    @BindView(R.id.button)
    Button mButton;
    private Context mContext;
    private BluetoothViewState vs;
    private BluetoothComponant bluetoothComponant;
    boolean lightIsOn;
    private final GattClient mGattClient = new GattClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        lightIsOn = false;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bluetooth;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGattClient.writeInteractor();
            }
        });

        String address = getActivity().getIntent().getStringExtra(EXTRA_DEVICE_ADDRESS);
        mGattClient.onCreate(mContext, address, mButton);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGattClient.onDestroy();
    }

    @Override
    public ViewState createViewState() {
        return new BluetoothViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (BluetoothViewState) viewState;
    }

    @Override
    public BluetoothPresenter createPresenter() {
        return bluetoothComponant.presenter();
    }

    @Override
    public void showForm() {
        vs.setShowForm();
    }

    @Override
    public void showError(String error) {
        vs.setShowError();
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();
    }

    @Override
    protected void injectDependencies() {
        bluetoothComponant = DaggerBluetoothComponant.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(BaseEvent event) {
        // Do stuff with Eventbus event
    }

}
