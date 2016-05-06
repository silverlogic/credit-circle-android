package com.tsl.baseapp.baseview;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.event.SignUpSuccessfulEvent;

import org.greenrobot.eventbus.Subscribe;

public class BaseFragment extends BaseViewStateFragment<BaseView, BasePresenter> implements BaseView {

    private Context mContext;
    private BaseViewState vs;
    private BaseComponant baseComponant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_base;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
    }

    @Override
    public ViewState createViewState() {
        return new BaseViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (BaseViewState) viewState;
    }

    @Override
    public BasePresenter createPresenter() {
        return baseComponant.presenter();
    }

    @Override
    public void showForm() {
        vs.setShowForm();
    }

    @Override
    public void showError() {
        vs.setShowError();
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();
    }

    @Override
    protected void injectDependencies() {
        baseComponant = DaggerBaseComponant.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(SignUpSuccessfulEvent event){
       // Do stuff with Eventbus event
    }

}
