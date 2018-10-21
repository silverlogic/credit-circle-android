package com.tsl.creditcircle.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.tsl.creditcircle.R;
import com.tsl.creditcircle.home.HomeFragment;
import com.tsl.creditcircle.loanRequest.LoanRequestFragment;
import com.tsl.creditcircle.utils.pushnotifications.FCMToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    FrameLayout mContainer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;
    public static Fragment mCurrentFragment;
    public static final String HOME_TAG = "homeTag";
    public static final String FIN_EDUCATION_TAG = "finEducationTag";
    public static final String GET_LOAN_TAG = "getLoanTag";
    public static final String CONTACTS_TAG = "contactsTag";
    public static final String PAY_TAG = "settingTag";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    openFragment(HomeFragment.newInstance(), HOME_TAG);
                    return true;
                case R.id.fin_ed:
                    openFragment(null, FIN_EDUCATION_TAG);
                    return true;
                case R.id.request:
                    openFragment(LoanRequestFragment.newInstance(), GET_LOAN_TAG);
                    return true;
                case R.id.contacts:
                    return true;
                case R.id.pay:
                    openFragment(null, PAY_TAG);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        setSupportActionBar(mToolbar);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mBottomNavigation.setSelectedItemId(R.id.home);
        FCMToken.uploadStoredTokenIfNeeded();
    }

    private void openFragment(Fragment fragment, String tag) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(null)
                    .commit();
    }

    public void setToolbarTitle(int title) {
        mToolbar.setTitle(title);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
