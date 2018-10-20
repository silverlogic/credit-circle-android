package com.tsl.money2020.forgotpassword;

import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.tsl.money2020.BuildConfig;
import com.tsl.money2020.R;
import com.tsl.money2020.RxSchedulersOverrideRule;
import com.tsl.money2020.api.BaseApi;

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
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;

import rx.Observable;
import static org.mockito.Mockito.when;

/**
 * Created by Kevin Lavi on 3/31/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ForgotPasswordPresenterTest {
    private ForgotPasswordPresenter presenter;
    @Mock
    private ForgotPasswordView view;
    @Mock
    Context mMockContext;
    @Mock
    private EventBus bus;
    @Mock
    BaseApi mApi;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    private ForgotPasswordActivity mForgotPasswordActivity;
    private ForgotPasswordFragment mForgotPasswordFragment;
    private Resources res;
    private EditText inputEmail;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new ForgotPasswordPresenter(bus, mApi);
        presenter.attachView(view);

        mForgotPasswordActivity = Robolectric.setupActivity(ForgotPasswordActivity.class);
        mForgotPasswordFragment = new ForgotPasswordFragment();
        SupportFragmentTestUtil.startVisibleFragment(mForgotPasswordFragment);
        res = mForgotPasswordActivity.getResources();
        inputEmail = mForgotPasswordFragment.mInputEmail;
    }

    @Test
    public void forgotPasswordSuccess() throws Exception {
        String email = "kl@tsl.io";
        JsonObject emailObject = new JsonObject();
        emailObject.addProperty("email", email);

        when(mApi.forgotPassword(Mockito.eq(emailObject))).thenReturn(Observable.<Void>just(null));

        presenter.forgotPassword(email, mMockContext);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).showSuccess();
    }

    @Test
    public void validate() throws Exception {
        // Arrange
        String givenEmail  = "kl@tsl.io";
        boolean wantedValid = true;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        valid = presenter.validateEmail(inputEmail, mForgotPasswordActivity);

        //assert
        assertEquals(wantedValid, valid);

    }

    @Test
    public void invalidEmailTest() throws Exception {
        // Arrange
        String givenEmail  = "not an email";
        String wantedError = res.getString(R.string.enter_valid_emil);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        valid = presenter.validateEmail(inputEmail, mForgotPasswordActivity);
        String error = inputEmail.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }

    @Test
    public void emptyEmailTest() throws Exception {
        // Arrange
        String givenEmail  = "";
        String wantedError = res.getString(R.string.enter_valid_emil);
        boolean wantedValid = false;
        boolean valid;

        //act
        inputEmail.setText(givenEmail);
        valid = presenter.validateEmail(inputEmail, mForgotPasswordActivity);
        String error = inputEmail.getError().toString();

        //assert
        assertEquals(wantedValid, valid);
        assertEquals(wantedError, error);
    }

}
