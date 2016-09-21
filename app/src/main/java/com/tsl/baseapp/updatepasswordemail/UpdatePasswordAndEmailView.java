package com.tsl.baseapp.updatepasswordemail;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/6/16.
 */
public interface UpdatePasswordAndEmailView extends MvpView{

    public void showChangePasswordForm();

    public void showUpdateEmailForm();

    public void showError(String error);

    public void showLoading();

    public void showChangePasswordSuccess();

    public void showUpdateEmailSuccess();

}
