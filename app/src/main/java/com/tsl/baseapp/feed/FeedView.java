package com.tsl.baseapp.feed;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by kevinlavi on 5/5/16.
 */
public interface FeedView extends MvpView {
    public void showFeed();

    public void showError();

    public void showLoading();

    public void fetchUsers();
}
