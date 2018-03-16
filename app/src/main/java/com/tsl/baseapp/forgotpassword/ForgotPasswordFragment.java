package com.tsl.baseapp.forgotpassword;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hkm.ui.processbutton.iml.ActionProcessButton;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.event.ForgotPasswordEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

import static com.tsl.baseapp.R.string.email;

public class ForgotPasswordFragment extends BaseViewStateFragment<ForgotPasswordView, ForgotPasswordPresenter> implements ForgotPasswordView {

    @BindView(R.id.input_email)
    EditText mInputEmail;
    @BindView(R.id.forgot_password_button)
    ActionProcessButton mSubmitButton;
    @BindView(R.id.forgot_password_form)
    LinearLayout mForgotPasswordForm;

    private Context mContext;
    private ForgotPasswordViewState vs;
    private ForgotPasswordComponent forgotPasswordComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mSubmitButton.setMode(ActionProcessButton.Mode.ENDLESS);
        mSubmitButton.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        }).build();
    }

    @Override
    public ViewState createViewState() {
        return new ForgotPasswordViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (ForgotPasswordViewState) viewState;
    }

    @Override
    public ForgotPasswordPresenter createPresenter() {
        return forgotPasswordComponent.presenter();
    }

    @Override
    public void showForm() {
        vs.setShowForm();
        setFormEnabled(true);
    }

    @Override
    public void showError() {
        setFormEnabled(true);
        vs.setShowError();
        String error = getString(R.string.error_forgot_password);
        showDialog(error);
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();
        setFormEnabled(false);
    }

    @Override
    public void showSuccess() {
        vs.setShowSuccess();
        setFormEnabled(true);
        mInputEmail.setText("");
        String message = getString(R.string.password_reset);
        showDialog(message);
    }

    private void forgotPassword(){
        boolean valid = presenter.validateEmail(mInputEmail, mContext);

        if (!valid) return;

        String email = mInputEmail.getText().toString();
        presenter.forgotPassword(email, mContext);
    }

    @Override
    protected void injectDependencies() {
        forgotPasswordComponent = DaggerForgotPasswordComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(ForgotPasswordEvent event) {
        // Do stuff with Eventbus event
    }

    private void setFormEnabled(boolean enabled) {
        mInputEmail.setEnabled(enabled);
        mSubmitButton.setEnabled(enabled);
    }

    private void showDialog(String s){
        new MaterialDialog.Builder(mContext)
                .content(s)
                .positiveText(R.string.ok)
                .show();
    }
}
