package com.tsl.money2020.feed;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tsl.money2020.model.objects.user.User;

import java.util.ArrayList;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class FeedViewState implements ViewState<FeedView> {
    final int STATE_SHOW_FEED= 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;
    final int STATE_FETCH_OBJECTS = 3;
    final int STATE_UPDATE_FEED = 4;
    final int STATE_SEARCH_RESULT = 5;

    int state = STATE_FETCH_OBJECTS;

    @Override public void apply(FeedView view, boolean retained) {

        switch (state) {
            case STATE_SHOW_LOADING:
                view.showLoading();
                break;

            case STATE_SHOW_ERROR:
                view.showError();
                break;

            case STATE_SHOW_FEED:
                view.showFeed();
                break;

            case STATE_FETCH_OBJECTS:
                view.fetchUsers();
                break;

            case STATE_UPDATE_FEED:
                view.updateFeed();
                break;

            case STATE_SEARCH_RESULT:
                view.onSearchResult(false, new ArrayList<User>());
                break;
        }
    }

    public void setShowFeed() {
        state = STATE_SHOW_FEED;
    }

    public void setShowError() {
        state = STATE_SHOW_ERROR;
    }

    public void setShowLoading() {
        state = STATE_SHOW_LOADING;
    }

    public void fetchUsers(){state = STATE_FETCH_OBJECTS;}

    public void updateFeed(){state = STATE_UPDATE_FEED;}

    public void searchResult(){state = STATE_SEARCH_RESULT;}
}
