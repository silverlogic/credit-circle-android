package com.tsl.baseapp.signup;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;

import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.model.objects.user.User1;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.KeyboardUtils;
import com.tsl.baseapp.model.event.SignUpSuccessfulEvent;
import com.tsl.baseapp.settings.SettingsActivity;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

public class SignUpFragment extends BaseViewStateFragment<SignUpView, SignUpPresenter> implements SignUpView {

    @Bind(R.id.input_first_name)
    EditText mInputFirstName;
    @Bind(R.id.input_last_name)
    EditText mInputLastName;
    @Bind(R.id.input_email)
    EditText mInputEmail;
    @Bind(R.id.input_password)
    EditText mInputPassword;
    @Bind(R.id.input_password_confirm)
    EditText mInputPasswordConfirm;
    @Bind(R.id.btn_signup)
    ActionProcessButton mSignUpButton;
    @Bind(R.id.link_login)
    TextView mLinkLogin;

    private SignUpComponent signUpComponent;
    private Context mContext;
    private SignUpViewState vs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSignUpButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mSignUpButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        }).build();
        mContext = getContext();
    }


    @Override
    public ViewState createViewState() {
        return new SignUpViewState();
    }

    @Override
    public SignUpPresenter createPresenter() {
        return signUpComponent.presenter();
    }


    @Override
    public void onNewViewStateInstance() {
        vs = (SignUpViewState) viewState;
        showSignUpForm();
    }


    @Override
    public void showSignUpForm() {
        vs.setShowSignUpForm();
        setFormEnabled(true);
        mSignUpButton.setProgress(0);
    }

    @Override
    public void showError() {
        vs.setShowError();
        setFormEnabled(true);
        mSignUpButton.setProgress(0);
        Toast.makeText(getActivity(), R.string.sign_up_failed, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();
        setFormEnabled(false);
        // any progress between 0 - 100 shows animation
        mSignUpButton.setProgress(30);
    }

    @Override
    public void signUpSuccessful() {
        mSignUpButton.setProgress(100); // We are done
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();
    }

    @Subscribe
    public void onEvent(SignUpSuccessfulEvent event){
        Hawk.put(Constants.USER, event.getUser());
    }

    private void setFormEnabled(boolean enabled) {
        mInputEmail.setEnabled(enabled);
        mInputPassword.setEnabled(enabled);
        mInputPasswordConfirm.setEnabled(enabled);
        mInputFirstName.setEnabled(enabled);
        mInputLastName.setEnabled(enabled);
        mSignUpButton.setEnabled(enabled);
        mLinkLogin.setEnabled(enabled);
    }

    @Override
    protected void injectDependencies() {
        signUpComponent = DaggerSignUpComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    private void signUp(){
        String email = mInputEmail.getText().toString();
        String password = mInputPassword.getText().toString();
        String confirm_password = mInputPasswordConfirm.getText().toString();
        String firstName = mInputFirstName.getText().toString();
        String lastName = mInputLastName.getText().toString();

        SignUpValidation validation = new SignUpValidation();
        boolean valid = validation.validate(mInputFirstName, mInputLastName, mInputEmail,
                mInputPassword, mInputPasswordConfirm, mContext);

        if (!valid) {
            return;
        }

        // Hide keyboard
        if (!KeyboardUtils.hideKeyboard(mInputEmail)) {
            KeyboardUtils.hideKeyboard(mInputPassword);
        }

        User1 user1 = new User1();
        user1.register(email, password, firstName, lastName);

        // Start signup
        presenter.doSignUp(user1);
    }

    @OnClick(R.id.link_login)
    public void signUpActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }
}
