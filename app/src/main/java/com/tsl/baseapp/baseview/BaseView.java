package com.tsl.baseapp.baseview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/6/16.
 */
public interface BaseView extends MvpView{
    public void showForm();

    public void showError(String error);

    public void showLoading();
}
