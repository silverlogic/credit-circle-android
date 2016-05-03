package com.tsl.baseapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jaredrummler.android.device.DeviceName;
import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.Controller.SignUpLoginController;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.model.Objects.user.User2;
import com.tsl.baseapp.model.Utilities.SaveSharedPreference;
import com.tsl.baseapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFrags())
                    .commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public class SettingsFrags extends PreferenceFragment implements SignUpLoginController.SignUpLoginCallBackListener {

        private EditText oldPass;
        private EditText newPass;
        private EditText confirmPass;
        private String pass;
        private String passNew;
        private String passConfirm;
        private View positiveAction;
        private SignUpLoginController mController;
        private ProgressDialog progressDialog;
        private String buildVersion;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);

            mController = new SignUpLoginController(this, getActivity());

            buildVersion = BuildConfig.VERSION_NAME;

            Preference mPrefProfile = findPreference("profile");
            mPrefProfile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (!SaveSharedPreference.isLoggedIn(getActivity())){
                        loginDialog();
                    }
                    else {
                        startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class));
                        overridePendingTransition(0, 0);
                    }


                    return false;
                }
            });

            Preference mPrefPassword = findPreference("password");
            mPrefPassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (!SaveSharedPreference.isLoggedIn(getActivity())) {
                        loginDialog();
                    } else {
                        changePassword();
                    }
                    return false;
                }
            });


            Preference mPrefLogin = findPreference("login");
            if (!SaveSharedPreference.isLoggedIn(getActivity())){
                mPrefLogin.setTitle("Login"); // Check if current user, if not current user change title to logout and logout method
                mPrefLogin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        login();
                        return false;
                    }
                });
            }
            else {
                    mPrefLogin.setTitle("Log out"); // Check if current user, if not current user change title to logout and logout method
                    mPrefLogin.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            logout();
                            return false;
                        }
                    });
                }

            Preference mPrefFeedback = findPreference("feedback");
            mPrefFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (!SaveSharedPreference.isLoggedIn(getActivity())){
                        String guest = "guest";
                        sendFeedback(guest, guest, guest);
                    }
                    else {
                        User2 mUser2 = SaveSharedPreference.getCurrentUser(getActivity());
                        String name = mUser2.getFirst_name() + " " + mUser2.getLast_name();
                        String email = mUser2.getEmail();
                        String userId = String.valueOf(mUser2.getId());
                        sendFeedback(name, email, userId);
                    }
                    return false;
                }
            });


            Preference mPrefVersion = (Preference) findPreference("version");
            mPrefVersion.setSummary(buildVersion);


        }


        private void changePassword(){
            final String password = SaveSharedPreference.getCurrentPassword(getActivity());
            Log.d("PASS:: ", password);

            boolean wrapInScrollView = true;
            final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .title("Change Password")
                    .customView(R.layout.change_password_view, wrapInScrollView)
                    .positiveText("SUBMIT")
                    .build();

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    oldPass.setEnabled(s.toString().trim().length() > 0);
                    newPass.setEnabled(s.toString().trim().length() > 0);
                    confirmPass.setEnabled(s.toString().trim().length() > 0);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
            //noinspection ConstantConditions
            oldPass = (EditText) dialog.getCustomView().findViewById(R.id.old_password);
            newPass = (EditText) dialog.getCustomView().findViewById(R.id.new_password);
            confirmPass = (EditText) dialog.getCustomView().findViewById(R.id.confirm_password);
            oldPass.addTextChangedListener(textWatcher);

            dialog.show();

            MDButton b = dialog.getActionButton(DialogAction.POSITIVE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass = oldPass.getText().toString();
                    passNew = newPass.getText().toString();
                    passConfirm = confirmPass.getText().toString();

                    if (pass.isEmpty() || !pass.equals(password)) {
                        oldPass.setError("Old password must match current password");
                    }
                    else if (!passNew.equals(passConfirm)) {
                        confirmPass.setError("New passwords must match");
                    }
                    else if (passNew.isEmpty()){
                        newPass.setError("Field cannot be empty");
                    }
                    else if (passConfirm.isEmpty()){
                        confirmPass.setError("Field cannot be empty");
                    }
                    else {
                        User2 mUser2 = new User2(pass, passNew, passConfirm);
                        //mController.changePassword(Constants.getToken(getActivity()), mUser2);
                        progressDialog = new ProgressDialog(getActivity(),
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Changing Password...");
                        progressDialog.show();
                        dialog.dismiss();
                    }
                }
            });

        }

        private void login(){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        }

        private void logout(){
            new MaterialDialog.Builder(getActivity())
                    .title("Log out")
                    .content("Are you sure?")
                    .positiveText("Yes").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
                    boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                    if (loggedIn) {
                        LoginManager.getInstance().logOut();
                    }
                    SaveSharedPreference.clearToken(getActivity());

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().overridePendingTransition(0, 0);
                }
            })
                    .negativeText("No").onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                }
            })
                    .show();
        }

        private void sendFeedback(String userName, String email, String userId){
            String deviceName = DeviceName.getDeviceName();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@fundthis.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "FundThis Android Feedback");
            i.putExtra(Intent.EXTRA_TEXT,
                    "\n" + "\n" + "\n" + "\n" +
                    "Name: " + userName + "\n" +
                            "Email: " + email + "\n" +
                            "ID: " + userId + "\n" +
                            "Version: " + buildVersion + "\n" +
                            "Device: " + deviceName);

            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        public void onLoginComplete(String token) {

        }

        @Override
        public void onLoginFailure() {

        }

        @Override
        public void onSignUpComplete() {
        }

        @Override
        public void onSignUpFailed() {

        }

        @Override
        public void onPassChanged() {
            progressDialog.dismiss();
            SaveSharedPreference.setCurrentPassword(getActivity(), passConfirm);
            Toast.makeText(getActivity(), "Password Changed!", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPassFailed() {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Password change failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFacebookLoggedIn(String token) {

        }

        private void loginDialog(){
            new MaterialDialog.Builder(getActivity())
                    .title("Login")
                    .content("You must log in to use this feature")
                    .positiveText("Login").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                }
            })
                    .negativeText("No Thanks").onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                }
            })
                    .show();
        }
    }

}
