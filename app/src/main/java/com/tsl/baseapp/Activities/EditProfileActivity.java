package com.tsl.baseapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.tsl.baseapp.Controller.SignUpLoginController;
import com.tsl.baseapp.Model.Objects.User;
import com.tsl.baseapp.Model.Utilities.Constants;
import com.tsl.baseapp.Model.Utilities.SaveSharedPreference;
import com.tsl.baseapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditProfileActivity extends AppCompatActivity implements SignUpLoginController.SignUpLoginCallBackListener {


    @Bind(R.id.edit_image)
    CircularImageView mEditImage;
    @Bind(R.id.edit_email)
    EditText mEditEmail;
    @Bind(R.id.edit_first_name)
    EditText mEditFirstName;
    @Bind(R.id.edit_last_name)
    EditText mEditLastName;
    @Bind(R.id.edit_facebook_url)
    EditText mEditFacebookUrl;
    @Bind(R.id.edit_instagram_url)
    EditText mEditInstagramUrl;
    @Bind(R.id.edit_linkedin_url)
    EditText mEditLinkedinUrl;
    @Bind(R.id.edit_tagline)
    EditText mEditTagline;
    @Bind(R.id.confirm_changes_button)
    Button mConfirmChangesButton;

    private SignUpLoginController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        mController = new SignUpLoginController(this, this);

        final User mUser = SaveSharedPreference.getCurrentUser(this);

        Picasso.with(this).load(mUser.getImg_url()).placeholder(R.drawable.ic_person_outline).into(mEditImage);

        mEditEmail.setText(mUser.getEmail());
        mEditFirstName.setText(mUser.getFirst_name());
        mEditLastName.setText(mUser.getLast_name());
        mEditFacebookUrl.setText(mUser.getFacebook());
        mEditInstagramUrl.setText(mUser.getInstagram());
        mEditLinkedinUrl.setText(mUser.getLinkedin());
        mEditTagline.setText(mUser.getTagline());


        mConfirmChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = mEditEmail.getText().toString();
                String newFirstName = mEditFirstName.getText().toString();
                String newLastName = mEditLastName.getText().toString();
                String newFacebookUrl = mEditFacebookUrl.getText().toString();
                String newInstagramUrl = mEditInstagramUrl.getText().toString();
                String newLinkedinUrl = mEditLinkedinUrl.getText().toString();
                String newTagline = mEditTagline.getText().toString();

                boolean isValidated = true;

                if (newFirstName.isEmpty()){
                    mEditFirstName.setError("First name must not be empty");
                    isValidated = false;
                }

                if(newLastName.isEmpty()){
                    mEditLastName.setError("Last name must not be empty");
                    isValidated = false;
                }

                if (newEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
                    mEditEmail.setError("Enter a valid email address");
                    isValidated = false;
                }

                if (!newFacebookUrl.isEmpty() && !URLUtil.isHttpsUrl(newFacebookUrl)){
                    mEditFacebookUrl.setError("Enter a valid https url");
                    isValidated = false;
                }

                if (!newInstagramUrl.isEmpty() && !URLUtil.isHttpsUrl(newInstagramUrl)){
                    mEditInstagramUrl.setError("Enter a valid https url");
                    isValidated = false;
                }

                if (!newLinkedinUrl.isEmpty() && !URLUtil.isHttpsUrl(newLinkedinUrl)){
                    mEditLinkedinUrl.setError("Enter a valid https url");
                    isValidated = false;
                }
                if (isValidated){

                    mUser.setEmail(newEmail);
                    mUser.setFirst_name(newFirstName);
                    mUser.setLast_name(newLastName);
                    mUser.setFacebook(newFacebookUrl);
                    mUser.setInstagram(newInstagramUrl);
                    mUser.setLinkedin(newLinkedinUrl);
                    mUser.setTagline(newTagline);

                    SaveSharedPreference.setCurrentUser(mUser, EditProfileActivity.this);
                    mController.updateUser(mUser, Constants.getToken(EditProfileActivity.this));
                }

            }
        });
    }

    @Override
    public void onLoginComplete(String token) {

    }

    @Override
    public void onLoginFailure() {

    }

    @Override
    public void onSignUpComplete() {
        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
        overridePendingTransition(0, 0);
    }

    @Override
    public void onSignUpFailed() {

    }

    @Override
    public void onPassChanged() {

    }

    @Override
    public void onPassFailed() {

    }

    @Override
    public void onFacebookLoggedIn(String token) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
