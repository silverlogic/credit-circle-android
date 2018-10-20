package com.tsl.money2020.signup;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.money2020.R;
import com.tsl.money2020.api.BaseApi;
import com.tsl.money2020.model.event.SignUpSuccessfulEvent;
import com.tsl.money2020.model.event.TokenEvent;
import com.tsl.money2020.model.objects.error.Error;
import com.tsl.money2020.model.objects.token.Token;
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.utils.RetrofitException;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by kevinlavi on 5/3/16.
 */
public class SignUpPresenter extends MvpBasePresenter<SignUpView> {

    private Subscription signUpSubscriber;
    private EventBus eventBus;
    private SignUpView view;
    private BaseApi api;

    @Inject
    public SignUpPresenter(EventBus eventBus, BaseApi api) {
        this.eventBus = eventBus;
        this.api = api;
        // push for bitrise
        //TODO remove this
    }

    public void doSignUp(final User credentials, final Context context) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        cancelSubscription();
        signUpSubscriber = api.signUpUser(credentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            if (e instanceof RetrofitException) {
                                RetrofitException error = (RetrofitException) e;
                                if (error.getKind() == RetrofitException.Kind.NETWORK) {
                                    //handle network error
                                    Timber.d("NETWORK ERROR");
                                    getView().showError(context.getString(R.string.no_internet));
                                } else {
                                    //handle error message from server
                                    Timber.d(e.getLocalizedMessage());
                                    Error response = null;
                                    try {
                                        response = error.getErrorBodyAs(Error.class);
                                        String errorString = response.getErrorString();
                                        Timber.d("Error = " + errorString);
                                        // FINISH API CALL
                                        getView().showError(response.getErrorString());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(final User user) {
                        signUpSubscriber = api.loginUser(credentials)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<Token>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (isViewAttached()) {
                                            if (e instanceof RetrofitException) {
                                                RetrofitException error = (RetrofitException) e;
                                                if (error.getKind() == RetrofitException.Kind.NETWORK) {
                                                    //handle network error
                                                    Timber.d("NETWORK ERROR");
                                                    getView().showError(context.getString(R.string.no_internet));
                                                } else {
                                                    //handle error message from server
                                                    Timber.d(e.getLocalizedMessage());
                                                    Error response = null;
                                                    try {
                                                        response = error.getErrorBodyAs(Error.class);
                                                        String errorString = response.getErrorString();
                                                        Timber.d("Error = " + errorString);
                                                        // FINISH API CALL
                                                        getView().showError(response.getErrorString());
                                                    } catch (IOException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNext(final Token token) {
                                        eventBus.post(new TokenEvent(token));
                                        signUpSubscriber = api.getCurrentUser(Constants.getToken())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(new Subscriber<User>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        getView().signUpSuccessful();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        if (isViewAttached()) {
                                                            if (e instanceof RetrofitException) {
                                                                RetrofitException error = (RetrofitException) e;
                                                                if (error.getKind() == RetrofitException.Kind.NETWORK) {
                                                                    //handle network error
                                                                    Timber.d("NETWORK ERROR");
                                                                    getView().showError(context.getString(R.string.no_internet));
                                                                } else {
                                                                    //handle error message from server
                                                                    Timber.d(e.getLocalizedMessage());
                                                                    Error response = null;
                                                                    try {
                                                                        response = error.getErrorBodyAs(Error.class);
                                                                        String errorString = response.getErrorString();
                                                                        Timber.d("Error = " + errorString);
                                                                        // FINISH API CALL
                                                                        getView().showError(response.getErrorString());
                                                                    } catch (IOException e1) {
                                                                        e1.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onNext(final User user) {
                                                        eventBus.post(new SignUpSuccessfulEvent(user));
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    public boolean validate(EditText inputEmail, EditText inputPassword, EditText inputConfirmPassword, Context context) {

        boolean valid = true;

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirm_pass= inputConfirmPassword.getText().toString();
        Context mContext = context;
        Resources r = mContext.getResources();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError(r.getString(R.string.enter_valid_emil));
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty()) {
            inputPassword.setError(r.getString(R.string.enter_password));
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        if (confirm_pass.isEmpty() || !confirm_pass.equals(password)) {
            inputConfirmPassword.setError(r.getString(R.string.passwords_must_match));
            valid = false;
        } else {
            inputConfirmPassword.setError(null);
        }

        return valid;
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (signUpSubscriber != null && !signUpSubscriber.isUnsubscribed()) {
            signUpSubscriber.unsubscribe();
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override public void attachView(SignUpView view) {
        super.attachView(view);
    }
}
