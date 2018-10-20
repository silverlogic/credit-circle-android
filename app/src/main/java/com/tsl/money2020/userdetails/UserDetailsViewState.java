package com.tsl.money2020.userdetails;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class UserDetailsViewState implements ViewState<UserDetailsView> {
    final int STATE_SHOW_FORM = 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;
    final int STATE_UPDATE_USER_SUCCESS = 3;
    final int STATE_FETCH_USER_SUCCESS = 4;

    int state = STATE_SHOW_FORM;

    @Override
    public void apply(UserDetailsView view, boolean retained) {
        switch (state) {
            case STATE_SHOW_LOADING:
                view.showLoading("message");
                break;

            case STATE_SHOW_ERROR:
                view.showError("error");
                break;

            case STATE_SHOW_FORM:
                view.showForm();
                break;

            case STATE_UPDATE_USER_SUCCESS:
                view.updateUserSuccess();
                break;

            case STATE_FETCH_USER_SUCCESS:
                view.fetchUserSuccess();
                break;
        }
    }

    public void setShowForm() {
        state = STATE_SHOW_FORM;
    }

    public void setShowError() {
        state = STATE_SHOW_ERROR;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }

    public void setUpdateUserSuccess() {
        state = STATE_UPDATE_USER_SUCCESS;
    }

    public void setFetchUserSuccess() {
        state = STATE_FETCH_USER_SUCCESS;
    }
}
