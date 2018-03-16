package com.tsl.baseapp.tutorial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.utils.Constants;

import agency.tango.materialintroscreen.SlideFragmentBuilder;


/**
 * Created by kevinlavi on 11/20/17.
 */

public class TutorialActivity extends MaterialTutorialActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSkipButtonVisible();
        enableLastSlideAlphaExitTransition(true);
        Intent intent = getIntent();

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.baseapp_logo)
                .title(getString(R.string.tutorial_title_1))
                .description(getString(R.string.tutorial_body_1))
                .build());

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            addSlide(new SlideFragmentBuilder()
                    .backgroundColor(R.color.colorAccent)
                    .buttonsColor(R.color.colorPrimary)
                    .image(R.drawable.ic_camera)
                    .title(getString(R.string.tutorial_title_2))
                    .description(getString(R.string.tutorial_title_2))
                    .possiblePermissions(new String[]{Manifest.permission.CAMERA})
                    .build());
        }

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.baseapp_logo)
                .title(getString(R.string.tutorial_title_3))
                .description(getString(R.string.tutorial_body_3))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorAccent)
                .buttonsColor(R.color.colorPrimary)
                .image(R.drawable.baseapp_logo)
                .title(getString(R.string.tutorial_title_4))
                .description(getString(R.string.tutorial_body_4))
                .build());

    }

    @Override
    public void onFinish() {
        Hawk.put(Constants.VIEWED_TUTORIAL, true);
        super.onFinish();
    }
}