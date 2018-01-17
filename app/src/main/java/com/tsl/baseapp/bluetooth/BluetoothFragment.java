package com.tsl.baseapp.bluetooth;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.event.BaseEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BluetoothFragment extends BaseViewStateFragment<BluetoothView, BluetoothPresenter> implements BluetoothView {

    @Bind(R.id.button)
    Button mButton;
    private Context mContext;
    private BluetoothViewState vs;
    private BluetoothComponant bluetoothComponant;
    boolean lightIsOn;

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
                if (!lightIsOn){
                    // turn light on
                    lightIsOn = true;
                    mButton.setBackgroundColor(getResources().getColor(R.color.red));
                    Toast.makeText(mContext, "Turn on light", Toast.LENGTH_SHORT).show();
                }
                else {
                    // turn light off
                    lightIsOn = false;
                    mButton.setBackgroundColor(getResources().getColor(R.color.green));
                    Toast.makeText(mContext, "Turn off light", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
