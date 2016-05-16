package com.tsl.baseapp.feed;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class FeedViewState implements ViewState<FeedView> {
    final int STATE_SHOW_FEED= 0;
    final int STATE_SHOW_LOADING = 1;
    final int STATE_SHOW_ERROR = 2;
    final int STATE_FETCH_PROJECTS = 3;

    int state = STATE_FETCH_PROJECTS;

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

            case STATE_FETCH_PROJECTS:
                view.fetchProjects();
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

    public void fetchProjects(){state = STATE_FETCH_PROJECTS;}
}
