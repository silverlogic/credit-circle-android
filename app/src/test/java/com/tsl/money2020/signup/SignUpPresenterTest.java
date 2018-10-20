package com.tsl.money2020.signup;

import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;

import com.tsl.money2020.BuildConfig;
import com.tsl.money2020.R;
import com.tsl.money2020.RxSchedulersOverrideRule;
import com.tsl.money2020.api.BaseApi;
import com.tsl.money2020.model.objects.token.Token;
import com.tsl.money2020.model.objects.user.User;

import org.greenrobot.eventbus.EventBus;
import org.junit.After;
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

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Kevin Lavi on 3/27/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SignUpPresenterTest {

    private SignUpPresenter presenter;
    @Mock
    private SignUpView view;
    @Mock
    Context mMockContext;
    @Mock
    private EventBus bus;
    @Mock
    BaseApi mApi;
    @Rule public MockitoRule rule = MockitoJUnit.rule();

    User user;
    Token token;
    private SignUpActivity mSignUpActivity;
    private SignUpFragment mSignUpFragment;
    private Resources res;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirm;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new SignUpPresenter(bus, mApi);
        presenter.attachView(view);
        user = new User();
        user.register("username@email.com", "1234");
        mSignUpActivity = Robolectric.setupActivity(SignUpActivity.class);
        mSignUpFragment = new SignUpFragment();
        SupportFragmentTestUtil.startVisibleFragment(mSignUpFragment);
        res = mSignUpActivity.getResources();
        inputEmail = mSignUpFragment.mInputEmail;
        inputPassword = mSignUpFragment.mInputPassword;
        inputPasswordConfirm = mSignUpFragment.mInputPasswordConfirm;
    }

    @Test
    public void signUpSuccess() throws Exception {
        // set dummy user, user output after signup and get user calls, and token after login

        User output = new User();
        output.setFirst_name("new");
        output.setLast_name("user");
        output.setId(1);

        token = new Token();
        token.setToken("Token 1234567890");

        when(mApi.signUpUser(Mockito.eq(user))).thenReturn(Observable.just(output));
        when(mApi.loginUser(Mockito.eq(user))).thenReturn(Observable.just(token));
        when(mApi.getCurrentUser(any(String.class))).thenReturn(Observable.just(output));

        presenter.doSignUp(user, mMockContext);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).signUpSuccessful();
    }

    @Test
    public void validate() throws Exception {
        // Arrange
        String givenPassword  = "12345";
        String givenPasswordConfirm  = "12345";
        String givenEmail  = "kl@tsl.io";
        boolean wantedValid = true;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        inputPasswordConfirm.setText(givenPasswordConfirm);
        valid = presenter.validate(inputEmail, inputPassword, inputPasswordConfirm, mSignUpActivity);

        //assert
        assertEquals(wantedValid, valid);

    }

    @Test
    public void invalidEmailTest() throws Exception {
        // Arrange
        String givenPassword  = "12345";
        String givenPasswordConfirm  = "12345";
        String givenEmail  = "not an email";
        String wantedError = res.getString(R.string.enter_valid_emil);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        inputPasswordConfirm.setText(givenPasswordConfirm);
        valid = presenter.validate(inputEmail, inputPassword, inputPasswordConfirm, mSignUpActivity);
        String error = inputEmail.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }

    @Test
    public void emptyEmailTest() throws Exception {
        // Arrange
        String givenPassword  = "12345";
        String givenPasswordConfirm  = "12345";
        String givenEmail  = "";
        String wantedError = res.getString(R.string.enter_valid_emil);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        inputPasswordConfirm.setText(givenPasswordConfirm);
        valid = presenter.validate(inputEmail, inputPassword, inputPasswordConfirm, mSignUpActivity);
        String error = inputEmail.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }

    @Test
    public void passwordsDoNotMatchTest() throws Exception {
        // Arrange
        String givenPassword  = "1234";
        String givenPasswordConfirm  = "12345";
        String givenEmail  = "kl@tsl.io";
        String wantedError = res.getString(R.string.passwords_must_match);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        inputPasswordConfirm.setText(givenPasswordConfirm);
        valid = presenter.validate(inputEmail, inputPassword, inputPasswordConfirm, mSignUpActivity);
        String error = inputPasswordConfirm.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);

    }

    @Test
    public void emptyPasswordTest() throws Exception {
        // Arrange
        String givenPassword  = "";
        String givenPasswordConfirm  = "12345";
        String givenEmail  = "kl@tsl.io";
        String wantedError = res.getString(R.string.enter_password);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        inputPassword.setText(givenPassword);
        inputPasswordConfirm.setText(givenPasswordConfirm);
        valid = presenter.validate(inputEmail, inputPassword, inputPasswordConfirm, mSignUpActivity);
        String error = inputPassword.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }

    @After public void tearDownMockito() {
        Mockito.validateMockitoUsage();
    }

    @After
    public void detachView() {
        presenter.detachView(false);
    }


}