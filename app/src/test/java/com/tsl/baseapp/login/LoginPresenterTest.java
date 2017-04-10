package com.tsl.baseapp.login;

import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;

import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.R;
import com.tsl.baseapp.RxSchedulersOverrideRule;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.SocialAuth;
import com.tsl.baseapp.model.objects.user.User;
import org.greenrobot.eventbus.EventBus;
import org.junit.Assert;
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
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import static org.junit.Assert.assertEquals;

import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Kevin Lavi on 3/31/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoginPresenterTest {
    private LoginPresenter presenter;
    @Mock
    private LoginView view;
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
    private LoginActivity mLoginActivity;
    private LoginFragment mLoginFragment;
    private Resources res;
    private EditText inputEmail;
    private EditText inputPassword;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(bus, mApi);
        presenter.attachView(view);
        user = new User();
        user.login("kl@tsl.io", "1234");

        mLoginActivity = Robolectric.setupActivity(LoginActivity.class);
        mLoginFragment = new LoginFragment();
        SupportFragmentTestUtil.startVisibleFragment(mLoginFragment);
        res = mLoginActivity.getResources();
        inputEmail = mLoginFragment.mInputEmail;
        inputPassword = mLoginFragment.mInputPassword;
    }

    @Test
    public void normalLoginSuccess() throws Exception {
        // set dummy user, user output after signup and get user calls, and token after login

        User output = new User();
        output.setFirst_name("new");
        output.setLast_name("user");
        output.setId(0);

        token = new Token();
        token.setToken("Token 1234567890");

        when(mApi.loginUser(Mockito.eq(user))).thenReturn(Observable.just(token));
        when(mApi.getCurrentUser(any(String.class))).thenReturn(Observable.just(output));

        presenter.doNormalLogin(mMockContext, user);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).loginSuccessful();
    }

    @Test
    public void oAuth2loginSuccess() throws Exception {
        // set dummy user, user output after signup and get user calls, and token after login

        User output = new User();
        output.setFirst_name("new");
        output.setLast_name("user");
        output.setId(0);

        SocialAuth socialAuth = SocialAuth.forFacebook(mMockContext, "facebookcode");

        token = new Token();
        token.setToken("Token 1234567890");

        when(mApi.socialLogin(Mockito.eq(socialAuth))).thenReturn(Observable.just(token));
        when(mApi.getCurrentUser(any(String.class))).thenReturn(Observable.just(output));

        presenter.doOAuthLogin(mMockContext, socialAuth);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).loginSuccessful();
    }

    @Test
    public void validate() throws Exception {
        // Arrange
        String givenPassword  = "12345";
        String givenEmail  = "kl@tsl.io";
        boolean wantedValid = true;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        valid = presenter.validate(inputEmail, inputPassword, mLoginActivity);

        //assert
        assertEquals(wantedValid, valid);

    }

    @Test
    public void invalidEmailTest() throws Exception {
        // Arrange
        String givenPassword  = "12345";
        String givenEmail  = "not an email";
        String wantedError = res.getString(R.string.enter_valid_emil);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        valid = presenter.validate(inputEmail, inputPassword, mLoginActivity);
        String error = inputEmail.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }

    @Test
    public void emptyEmailTest() throws Exception {
        // Arrange
        String givenPassword  = "12345";
        String givenEmail  = "";
        String wantedError = res.getString(R.string.enter_valid_emil);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        valid = presenter.validate(inputEmail, inputPassword, mLoginActivity);
        String error = inputEmail.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }

    @Test
    public void emptyPasswordTest() throws Exception {
        // Arrange
        String givenPassword  = "";
        String givenEmail  = "kl@tsl.io";
        String wantedError = res.getString(R.string.enter_password);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        valid = presenter.validate(inputEmail, inputPassword, mLoginActivity);
        String error = inputPassword.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }
}
