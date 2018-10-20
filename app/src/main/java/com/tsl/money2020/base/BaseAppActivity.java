package com.tsl.money2020.base;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tsl.money2020.R;
import com.tsl.money2020.utils.Utils;

import butterknife.ButterKnife;
import icepick.Icepick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Base class for Activities which already setup butterknife and icepick
 *
 *
 */
public class BaseAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.isNetworkAvailable(this)){
            // no internet. Let user know
            Snackbar.make(findViewById(android.R.id.content), R.string.no_internet,Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected void injectDependencies() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}