package com.tsl.baseapp.updatepasswordemail;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class UpdatePasswordAndEmailViewState implements ViewState<UpdatePasswordAndEmailView> {
    final int STATE_SHOW_CHANGE_PASSWORD_FORM = 0;
    final int STATE_SHOW_UPDATE_EMAIL_FORM = 3;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;
    final int STATE_SHOW_CHANGE_PASSWORD_SUCCESS = 4;
    final int STATE_SHOW_UPDATE_EMAIL_SUCCESS = 5;
    String errorMessage;

    int state = STATE_SHOW_CHANGE_PASSWORD_FORM;

    @Override
    public void apply(UpdatePasswordAndEmailView view, boolean retained) {
        switch (state) {
            case STATE_SHOW_LOADING:
                view.showLoading();
                break;

            case STATE_SHOW_ERROR:
                view.showError(errorMessage);
                break;

            case STATE_SHOW_CHANGE_PASSWORD_FORM:
                view.showChangePasswordForm();
                break;

            case STATE_SHOW_UPDATE_EMAIL_FORM:
                view.showUpdateEmailForm();
                break;

            case STATE_SHOW_CHANGE_PASSWORD_SUCCESS:
                view.showChangePasswordSuccess(false);
                break;

            case STATE_SHOW_UPDATE_EMAIL_SUCCESS:
                view.showUpdateEmailSuccess();
                break;

        }
    }

    public void setShowChangePasswordForm() {
        state = STATE_SHOW_CHANGE_PASSWORD_FORM;
    }

    public void setUpdateEmailForm() {
        state = STATE_SHOW_UPDATE_EMAIL_SUCCESS;
    }

    public void setShowError(String error) {
        state = STATE_SHOW_ERROR;
        errorMessage = error;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }

    public void setShowChangePasswordSuccess() {
        state = STATE_SHOW_CHANGE_PASSWORD_SUCCESS;
    }

    public void setUpdateEmailSuccess() {
        state = STATE_SHOW_UPDATE_EMAIL_SUCCESS;
    }
}
