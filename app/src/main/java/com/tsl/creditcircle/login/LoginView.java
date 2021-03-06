package com.tsl.creditcircle.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 4/26/16.
 */
public interface LoginView extends MvpView {
    public void showLoginForm();

    public void showError(String error);

    public void showLoading();

    public void loginSuccessful();

    public void twitterLogin();
}
