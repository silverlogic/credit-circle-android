package com.tsl.baseapp.feed;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.model.objects.user.UserList;

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
