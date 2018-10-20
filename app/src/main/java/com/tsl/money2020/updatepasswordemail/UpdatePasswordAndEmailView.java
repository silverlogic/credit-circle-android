package com.tsl.money2020.updatepasswordemail;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/6/16.
 */
public interface UpdatePasswordAndEmailView extends MvpView{

    public void showChangePasswordForm();

    public void showUpdateEmailForm();

    public void showError(String error);

    public void showLoading();

    public void showChangePasswordSuccess(boolean reset);

    public void showUpdateEmailSuccess();

    public void showChangingEmailLoading(String body);

    public void showConfirmEmailSuccess(String message);

    public void showVerifyEmailSuccess(String message);

    public void showChangingEmailFailed(String error);

    public void showLoadingUserHasVerifiedEmail();

    public void showUserHasVerifiedEmail();

    public void showErrorUserHasVerifiedEmail(String error);
}
