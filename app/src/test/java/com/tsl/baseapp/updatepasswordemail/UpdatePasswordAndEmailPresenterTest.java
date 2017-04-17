package com.tsl.baseapp.updatepasswordemail;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.R;
import com.tsl.baseapp.RxSchedulersOverrideRule;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.login.LoginFragment;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.signup.SignUpPresenter;
import com.tsl.baseapp.signup.SignUpView;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.tsl.baseapp.R.string.email;
import static org.junit.Assert.assertEquals;

import butterknife.ButterKnife;
import rx.Observable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.buildActivity;

/**
 * Created by Kevin Lavi on 3/31/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UpdatePasswordAndEmailPresenterTest {

    private UpdatePasswordAndEmailPresenter presenter;
    @Mock
    private UpdatePasswordAndEmailView view;
    @Mock
    Context mMockContext;
    @Mock
    private EventBus bus;
    @Mock
    BaseApi mApi;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    User user;
    Token token;

    private UpdatePasswordAndEmailActivity mUpdatePasswordAndEmailActivity;
    private UpdatePasswordAndEmailFragment mUpdatePasswordAndEmailFragment;

    private Resources res;

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private EditText changeEmailInput;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new UpdatePasswordAndEmailPresenter(bus, mApi);
        presenter.attachView(view);
        user = new User();
        user.register("username@email.com", "1234");

        Intent intent = new Intent(ShadowApplication.getInstance().getApplicationContext(), UpdatePasswordAndEmailActivity.class);
        intent.putExtra("type", "updateEmail");
        mUpdatePasswordAndEmailActivity = Robolectric.buildActivity(UpdatePasswordAndEmailActivity.class, intent).create().get();

        res = mUpdatePasswordAndEmailActivity.getResources();

        Bundle bundle = new Bundle();
        bundle.putString(UpdatePasswordAndEmailFragment.TYPE, UpdatePasswordAndEmailFragment.UPDATE_EMAIL);
        mUpdatePasswordAndEmailFragment = new UpdatePasswordAndEmailFragment();
        mUpdatePasswordAndEmailFragment.setArguments(bundle);
        SupportFragmentTestUtil.startFragment(mUpdatePasswordAndEmailFragment, AppCompatActivity.class);

        changeEmailInput = mUpdatePasswordAndEmailFragment.mChangeEmailInput;
        currentPassword = mUpdatePasswordAndEmailFragment.mCurrentPassword;
        newPassword = mUpdatePasswordAndEmailFragment.mNewPassword;
        confirmNewPassword = mUpdatePasswordAndEmailFragment.mConfirmNewPassword;
    }

    @Test
    public void testPutExtras() throws NullPointerException{
        String str = mUpdatePasswordAndEmailFragment.getArguments().getString(UpdatePasswordAndEmailFragment.TYPE);
        assertNotNull(str);
        assertEquals(str, "updateEmail");
    }

    @Test
    public void validateEmailCorrect() throws Exception {
        // Arrange
        String givenEmail  = "kl@tsl.io";
        boolean wantedValid = true;
        boolean valid;

        //act
        changeEmailInput.setText(givenEmail);
        valid = presenter.validateEmail(changeEmailInput, mUpdatePasswordAndEmailActivity);

        //assert
        assertEquals(wantedValid, valid);

    }

    @Test
    public void validateEmailNotCorrect() throws Exception {
        // Arrange
        String givenEmail  = "kl";
        boolean wantedValid = false;
        boolean valid;

        //act
        changeEmailInput.setText(givenEmail);
        valid = presenter.validateEmail(changeEmailInput, mUpdatePasswordAndEmailActivity);

        //assert
        assertEquals(wantedValid, valid);
    }

    @Test
    public void emptyEmailTest() throws Exception {
        // Arrange
        String givenEmail  = "";
        boolean wantedValid = false;
        boolean valid;

        //act
        changeEmailInput.setText(givenEmail);
        valid = presenter.validateEmail(changeEmailInput, mUpdatePasswordAndEmailActivity);

        //assert
        assertEquals(wantedValid, valid);
    }

    @Test
    public void validatePasswords() throws Exception {
        // Arrange
        String givenPassword  = "1234";
        String givenPasswordConfirm  = "1234";
        boolean wantedValid = true;
        boolean valid;

        //act
        newPassword.setText(givenPassword);
        confirmNewPassword.setText(givenPasswordConfirm);
        valid = presenter.validatePasswords(newPassword, confirmNewPassword, mUpdatePasswordAndEmailActivity);

        //assert
        assertEquals(wantedValid, valid);
    }

    @Test
    public void emptyPasswordTest() throws Exception {
        //Arrange
        String givenPassword  = "";
        String givenPasswordConfirm  = "1234";
        boolean wantedValid = false;
        boolean valid;

        //Act
        newPassword.setText(givenPassword);
        confirmNewPassword.setText(givenPasswordConfirm);
        valid = presenter.validatePasswords(newPassword, confirmNewPassword, mUpdatePasswordAndEmailActivity);

        //Assert
        assertEquals(wantedValid, valid);
    }

    @Test
    public void emptyConfirmPasswordTest() throws Exception {
        //Arrange
        String givenPassword  = "1234";
        String givenPasswordConfirm  = "";
        boolean wantedValid = false;
        boolean valid;

        //Act
        newPassword.setText(givenPassword);
        confirmNewPassword.setText(givenPasswordConfirm);
        valid = presenter.validatePasswords(newPassword, confirmNewPassword, mUpdatePasswordAndEmailActivity);

        //Assert
        assertEquals(wantedValid, valid);
    }

    @Test
    public void passwordsDoNotMatchTest() throws Exception {
        //Arrange
        String givenPassword  = "1234";
        String givenPasswordConfirm  = "12345";
        boolean wantedValid = false;
        boolean valid;

        //Act
        newPassword.setText(givenPassword);
        confirmNewPassword.setText(givenPasswordConfirm);
        valid = presenter.validatePasswords(newPassword, confirmNewPassword, mUpdatePasswordAndEmailActivity);

        //Assert
        assertEquals(wantedValid, valid);
    }


    @Test
    public void updateEmailSuccess() throws Exception {
        User user = new User();
        user.changeEmail("kl@tsl.io");

        token = new Token();
        token.setToken("Token 1234567890");

        when(mApi.changeEmail(any(String.class), Mockito.eq(user))).thenReturn(Observable.<Void>just(null));

        presenter.updateEmail(token.getToken(), user, mMockContext);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).showUpdateEmailSuccess();
    }


    @Test
    public void changePasswordSuccess() throws Exception {
        User user = new User();
        user.changePassword("123", "1234");

        token = new Token();
        token.setToken("Token 1234567890");

        JsonObject object = new JsonObject();

        when(mApi.changePassword(any(String.class), Mockito.eq(user))).thenReturn(Observable.just(object));

        presenter.changePassword(token.getToken(), user, mMockContext);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).showChangePasswordSuccess(false);
    }

}
