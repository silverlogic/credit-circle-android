package com.tsl.money2020.userdetails;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/6/16.
 */
public interface UserDetailsView extends MvpView{
    public void showForm();

    public void showError(String error);

    public void showLoading(String message);

    public void updateUserSuccess();

    public void fetchUserSuccess();
}
