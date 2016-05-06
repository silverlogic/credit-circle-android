package com.tsl.baseapp.crop;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class CropViewState implements ViewState<CropView> {
    final int STATE_SHOW_FORM = 0;

    int state = STATE_SHOW_FORM;

    @Override
    public void apply(CropView view, boolean retained) {
        switch (state) {
            case STATE_SHOW_FORM:
                view.showForm();
                break;

        }
    }

    public void setShowForm() {
        state = STATE_SHOW_FORM;
    }

}
