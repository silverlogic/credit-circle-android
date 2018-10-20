package com.tsl.money2020.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tsl.money2020.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                    openFragment(HOME_TAG);
                    return true;
                case R.id.fin_ed:
                    openFragment(FIN_EDUCATION_TAG);
                    return true;
                case R.id.request:
                    openFragment(GET_LOAN_TAG);
                    return true;
                case R.id.contacts:
                    openFragment(CONTACTS_TAG);
                    return true;
                case R.id.pay:
                    openFragment(PAY_TAG);
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
    }

    private void openFragment(String tag) {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//        if (fragment == null) {
//            fragment = getFragment(tag);
//        }
//        if (mCurrentFragment != null
//                && mCurrentFragment.getTag() != null
//                && mCurrentFragment.getTag().equals(RECORD_TAG)
//                && RecordFragment.mFileName != null){
//            // we are on record fragment. Show dialog
//            showLeavingDialog(fragment, tag);
//        }
//        else {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment, tag)
//                    .addToBackStack(null)
//                    .commit();
//        }

        Toast.makeText(this, tag, Toast.LENGTH_SHORT).show();
    }

    public void setToolbarTitle(int title) {
        mToolbar.setTitle(title);
    }

}
