package com.tsl.money2020.feed;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tsl.money2020.model.objects.user.User;

import java.util.List;

/**
 * Created by kevinlavi on 5/5/16.
 */
public interface FeedView extends MvpView {
    public void showFeed();

    public void showError();

    public void showLoading();

    public void fetchUsers();

    public void updateFeed();

    public void onSearchResult(boolean update, List<User> users);
}
