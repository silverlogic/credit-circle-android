package com.tsl.baseapp.utils.viewhelper;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.hkm.ui.processbutton.iml.ActionProcessButton;

/**
 * Created by Kevin Lavi on 9/2/16.
 */

public class ActionProcessButtonChangeText extends ActionProcessButton {
    protected CharSequence mNormalText;

    private Mode mMode;

    private int mColor1;
    private int mColor2;
    private int mColor3;
    private int mColor4;

    public ActionProcessButtonChangeText(Context context) {
        super(context);
    }

    public ActionProcessButtonChangeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActionProcessButtonChangeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources res = context.getResources();
        mMode = Mode.ENDLESS;
        mColor1 = res.getColor(com.hkm.ui.processbutton.R.color.holo_blue_bright);
        mColor2 = res.getColor(com.hkm.ui.processbutton.R.color.holo_green_light);
        mColor3 = res.getColor(com.hkm.ui.processbutton.R.color.holo_orange_light);
        mColor4 = res.getColor(com.hkm.ui.processbutton.R.color.holo_red_light);
    }

    @Override
    public CharSequence getNormalText() {
        return mNormalText;
    }

    public void setNormalText(String text){
        mNormalText = text;
    }
}
