package com.tsl.baseapp.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseAppActivity;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.webview.WebViewActivity;

/**
 * Created by kevinlavi on 5/4/16.
 */
public class SettingsActivity extends BaseAppActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        Preference mAbout;
        Preference mTerms;
        Preference mFeedback;
        Preference mChangePassword;
        Preference mLogout;

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            setPrefs();
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == mAbout){
                about();
            }
            if (preference == mTerms){
                policy();
            }
            if (preference == mFeedback){
                feedback();
            }
            if (preference == mChangePassword){
                changePassword();
            }
            if (preference == mLogout){
                logout();
            }
            return false;
        }

        private void setPrefs(){
            mAbout = findPreference("about");
            mTerms = findPreference("terms");
            mFeedback = findPreference("feedback");
            mChangePassword = findPreference("change_password");
            mLogout = findPreference("logout");

            mAbout.setOnPreferenceClickListener(this);
            mTerms.setOnPreferenceClickListener(this);
            mFeedback.setOnPreferenceClickListener(this);
            mChangePassword.setOnPreferenceClickListener(this);
            mLogout.setOnPreferenceClickListener(this);
        }

        private void about(){
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            this.startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }

        private void policy(){
            String mTermsLink = getString(R.string.terms_link);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String terms = getString(R.string.terms_of_services);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(Constants.URL, mTermsLink);
                intent.putExtra(Constants.TITLE, terms);
                this.startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTermsLink));
                this.startActivity(browserIntent);
            }
        }

        private void feedback(){
            User mUser = Hawk.get(Constants.USER);
            SendFeedBack sendFeedBack = new SendFeedBack();
            sendFeedBack.send(mUser, getActivity());
        }

        private void changePassword(){
            String token = Hawk.get(Constants.TOKEN);
            ChangePasswordDialog dialog = new ChangePasswordDialog();
            dialog.setDialog(getActivity(), token);
        }

        private void logout(){
            LogOutDialog dialog = new LogOutDialog();
            dialog.logout(getActivity());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
