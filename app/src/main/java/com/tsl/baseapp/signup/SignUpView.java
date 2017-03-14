package com.tsl.baseapp.signup;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/3/16.
 */
public interface SignUpView extends MvpView {
    public void showSignUpForm();

    public void showError(String error);

    public void showLoading();

    public void signUpSuccessful();
}
