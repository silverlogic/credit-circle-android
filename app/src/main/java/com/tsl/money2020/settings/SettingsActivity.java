package com.tsl.money2020.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.orhanobut.hawk.Hawk;
import com.tsl.money2020.BuildConfig;
import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseAppActivity;
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.model.objects.user.UserFinder;
import com.tsl.money2020.tutorial.TutorialActivity;
import com.tsl.money2020.updatepasswordemail.UpdatePasswordAndEmailActivity;
import com.tsl.money2020.updatepasswordemail.UpdatePasswordAndEmailFragment;
import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.utils.Utils;
import com.tsl.money2020.webview.WebViewActivity;

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
        Preference mChangeEmail;
        Preference mChangePassword;
        Preference mViewTutorial;
        Preference mLogout;
        Preference mVersion;

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            setPrefs();
            setVersion();
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
            if (preference == mChangeEmail){
                changeEmail();
            }
            if (preference == mViewTutorial){
                viewTutorial();
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
            mViewTutorial = findPreference("view_tutorial");
            mLogout = findPreference("logout");
            mChangeEmail = findPreference("change_email");
            mVersion = findPreference("version");


            mAbout.setOnPreferenceClickListener(this);
            mTerms.setOnPreferenceClickListener(this);
            mFeedback.setOnPreferenceClickListener(this);
            mChangePassword.setOnPreferenceClickListener(this);
            mLogout.setOnPreferenceClickListener(this);
            mChangeEmail.setOnPreferenceClickListener(this);
            mViewTutorial.setOnPreferenceClickListener(this);
        }

        void setVersion(){
            String buildVersion = "v" + BuildConfig.VERSION_NAME;
            mVersion.setSummary(buildVersion);
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
            int userID = Hawk.get(Constants.USER_ID);
            User user = UserFinder.find(userID);
            SendFeedBack sendFeedBack = new SendFeedBack();
            sendFeedBack.send(user, getActivity());
        }

        private void changeEmail(){
            Intent intent = new Intent(getActivity(), UpdatePasswordAndEmailActivity.class);
            intent.putExtra(UpdatePasswordAndEmailFragment.TYPE, UpdatePasswordAndEmailFragment.UPDATE_EMAIL);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }

        private void changePassword(){
            Intent intent = new Intent(getActivity(), UpdatePasswordAndEmailActivity.class);
            intent.putExtra(UpdatePasswordAndEmailFragment.TYPE, UpdatePasswordAndEmailFragment.CHANGE_PASSWORD);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }

        private void viewTutorial(){
            Utils.startActivity(getActivity(), TutorialActivity.class, false);
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
