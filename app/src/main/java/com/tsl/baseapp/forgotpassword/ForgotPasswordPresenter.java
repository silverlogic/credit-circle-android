package com.tsl.baseapp.forgotpassword;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.R;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.event.ForgotPasswordEvent;
import com.tsl.baseapp.model.objects.error.Error;
import com.tsl.baseapp.utils.RetrofitException;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class ForgotPasswordPresenter extends MvpBasePresenter<ForgotPasswordView> {

    private Subscription baseSubscriber;
    private EventBus eventBus;

    @Inject
    public ForgotPasswordPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void forgotPassword(String email, final Context context) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        JsonObject emailObject = new JsonObject();
        emailObject.addProperty("email", email);
        String message = "";

        // MAKE THE API CALL HERE
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
        baseSubscriber = api.forgotPassword(emailObject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            if (e instanceof RetrofitException) {
                                RetrofitException error = (RetrofitException) e;
                                if (error.getKind() == RetrofitException.Kind.NETWORK) {
                                    //handle network error
                                    Timber.d("NETWORK ERROR");
                                } else {
                                    //handle error message from server
                                    Timber.d(e.getLocalizedMessage());
                                    Error response = null;
                                    try {
                                        response = error.getErrorBodyAs(Error.class);
                                        String errorString = response.getErrorString();
                                        Timber.d("Error = " + errorString);
                                        // FINISH API CALL
                                        getView().showError();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }


    public boolean validateForgetPassword(EditText inputEmail, Context context) {
        boolean valid = true;

        String email = inputEmail.getText().toString();

        Context mContext = context;
        Resources r = mContext.getResources();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError(r.getString(R.string.valid_email_error));
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        return valid;
    }


    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (baseSubscriber != null && !baseSubscriber.isUnsubscribed()) {
            baseSubscriber.unsubscribe();
        }
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override
    public void attachView(ForgotPasswordView view) {
        super.attachView(view);
    }
}
