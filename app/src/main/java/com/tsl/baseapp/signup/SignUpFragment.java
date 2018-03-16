package com.tsl.baseapp.signup;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
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
import com.tsl.baseapp.feed.FeedActivity;
import com.tsl.baseapp.login.LoginActivity;
import com.tsl.baseapp.model.event.TokenEvent;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.KeyboardUtils;
import com.tsl.baseapp.model.event.SignUpSuccessfulEvent;
import com.tsl.baseapp.utils.Utils;
import com.tsl.baseapp.utils.Writer;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class SignUpFragment extends BaseViewStateFragment<SignUpView, SignUpPresenter> implements SignUpView {

    @BindView(R.id.input_first_name)
    EditText mInputFirstName;
    @BindView(R.id.input_last_name)
    EditText mInputLastName;
    @BindView(R.id.input_email)
    EditText mInputEmail;
    @BindView(R.id.input_password)
    EditText mInputPassword;
    @BindView(R.id.input_password_confirm)
    EditText mInputPasswordConfirm;
    @BindView(R.id.btn_signup)
    ActionProcessButton mSignUpButton;
    @BindView(R.id.link_login)
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
                validate();
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
        // set password form to hide inputs
        mInputPassword.setTransformationMethod(new PasswordTransformationMethod());

        mInputPasswordConfirm.setTransformationMethod(new PasswordTransformationMethod());
        mInputPasswordConfirm.setOnEditorActionListener(Utils.closeKeyboardOnEnter(mContext));

        mSignUpButton.setProgress(0);
        setFormEnabled(true);
    }

    @Override
    public void showError(String error) {
        vs.setShowError();
        setFormEnabled(true);
        mSignUpButton.setProgress(0);
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
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
        vs.setSignUpSuccess();
        mSignUpButton.setProgress(100); // We are done
        Utils.startActivity(getActivity(), FeedActivity.class, true);
        getActivity().finish();
    }

    private void validate(){
        boolean valid = presenter.validate(mInputEmail, mInputPassword, mInputPasswordConfirm, mContext);

        if (!valid) return;

        String email = mInputEmail.getText().toString();
        String password = mInputPassword.getText().toString();
        String firstName = mInputFirstName.getText().toString();
        String lastName = mInputLastName.getText().toString();

        // Hide keyboard
        if (!KeyboardUtils.hideKeyboard(mInputEmail)) {
            KeyboardUtils.hideKeyboard(mInputPassword);
        }

        User user = new User();
        user.register(email, password);
        if (!firstName.isEmpty()) user.setFirst_name(firstName);
        if (!lastName.isEmpty()) user.setLast_name(lastName);

        // Start signup
        presenter.doSignUp(user, mContext);
    }

    @Subscribe
    public void onEvent(SignUpSuccessfulEvent event){
        User user = event.getUser();
        // persist user id for fetching from realms
        Hawk.put(Constants.USER_ID, user.getId());
        // persist current user
        Writer.persist(user);

    }

    @Subscribe
    public void onEvent(TokenEvent event){
        Hawk.put(Constants.TOKEN, event.getToken());
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

    @OnClick(R.id.link_login)
    public void loginActivity(){
        Utils.startActivity(getActivity(), LoginActivity.class, false);
    }
}
