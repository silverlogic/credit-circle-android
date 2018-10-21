package com.tsl.creditcircle.forgotpassword;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/6/16.
 */
public interface ForgotPasswordView extends MvpView{
    public void showForm();

    public void showError();

    public void showLoading();

    public void showSuccess();
}
