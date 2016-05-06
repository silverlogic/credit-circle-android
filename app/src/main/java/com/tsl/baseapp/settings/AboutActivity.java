package com.tsl.baseapp.settings;

import android.os.Bundle;
import android.view.View;

import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseAppActivity;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String website = getString(R.string.website_url);
        String facebook = getString(R.string.facebook_link);
        String twitter = getString(R.string.twitter_link);
        String instagram = getString(R.string.instagram_link);
        String buildVersion = BuildConfig.VERSION_NAME;

        Element versionElement = new Element();
        versionElement.setTitle("Version " + buildVersion);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.baseapp_logo)
                .addItem(versionElement)
                .addWebsite(website)
                .addFacebook(facebook)
                .addTwitter(twitter)
                .addInstagram(instagram)
                //.addPlayStore("com.ideashower.readitlater.pro")
                .create();

        setContentView(aboutPage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
