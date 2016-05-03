package com.tsl.baseapp.signup;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * Created by kevinlavi on 5/3/16.
 */
public class SignUpViewState implements ViewState<SignUpView> {

    final int STATE_SHOW_SIGN_UP_FORM = 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;

    int state = STATE_SHOW_SIGN_UP_FORM;

    @Override
    public void apply(SignUpView view, boolean retained) {

        switch (state) {
            case STATE_SHOW_LOADING:
                view.showLoading();
                break;

            case STATE_SHOW_ERROR:
                view.showError();
                break;

            case STATE_SHOW_SIGN_UP_FORM:
                view.showSignUpForm();
                break;

        }
    }

    public void setShowSignUpForm() {
        state = STATE_SHOW_SIGN_UP_FORM;
    }

    public void setShowError() {
        state = STATE_SHOW_ERROR;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }
}
