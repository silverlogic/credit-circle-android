package com.tsl.baseapp.updatepasswordemail;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;
import android.widget.EditText;

import com.google.common.base.Converter;
import com.google.gson.JsonObject;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.R;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.objects.error.Error;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.RetrofitException;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.tsl.baseapp.R.id.current_password;

/**
 * Created by kevinlavi on 5/6/16.
 */
public class UpdatePasswordAndEmailPresenter extends MvpBasePresenter<UpdatePasswordAndEmailView> {

    private Subscription subscription;
    private EventBus eventBus;
    private BaseApi api;

    @Inject
    public UpdatePasswordAndEmailPresenter(EventBus eventBus, BaseApi api) {
        this.eventBus = eventBus;
        this.api = api;
    }

    public void updateEmail(String token, User credentials, final Context context) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        cancelSubscription();
        subscription = api.changeEmail(token, credentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showUpdateEmailSuccess();
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
                                    getView().showError(context.getString(R.string.no_internet));
                                } else {
                                    //handle error message from server
                                    Timber.d(e.getLocalizedMessage());
                                    Error response = null;
                                    try {
                                        response = error.getErrorBodyAs(Error.class);
                                        String errorString = response.getErrorString();
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
                    public void onNext(Void aVoid) {
                    }
                });
    }

    public void confirmEmail(int id, User user, final Context context) {

        if (isViewAttached()) {
            getView().showChangingEmailLoading(context.getString(R.string.confirming_email));
        }

        cancelSubscription();
        subscription = api.changeEmailConfirm(id, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showConfirmEmailSuccess(context.getString(R.string.confirm_email_success));
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
                                    getView().showError(context.getString(R.string.no_internet));
                                } else {
                                    //handle error message from server
                                    Timber.d(e.getLocalizedMessage());
                                    Error response = null;
                                    try {
                                        response = error.getErrorBodyAs(Error.class);
                                        String errorString = response.getErrorString();
                                        // FINISH API CALL
                                        getView().showChangingEmailFailed(response.getErrorString());
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

    public void verifyEmail(int id, User user, final Context context) {

        if (isViewAttached()) {
            getView().showChangingEmailLoading(context.getString(R.string.verifying_email));
        }

        cancelSubscription();
        subscription = api.changeEmailVerify(id, user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showVerifyEmailSuccess(context.getString(R.string.verify_email_success));
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
                                    getView().showError(context.getString(R.string.no_internet));
                                } else {
                                    //handle error message from server
                                    Timber.d(e.getLocalizedMessage());
                                    Error response = null;
                                    try {
                                        response = error.getErrorBodyAs(Error.class);
                                        String errorString = response.getErrorString();
                                        // FINISH API CALL
                                        getView().showChangingEmailFailed(response.getErrorString());
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

    public void changePassword(String token, User creds, final Context context){

        if (isViewAttached()) {
            getView().showLoading();
        }

        cancelSubscription();
        subscription = api.changePassword(token, creds)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showChangePasswordSuccess(false);
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
                                        getView().showError(response.getErrorString());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(JsonObject object) {
                    }
                });
    }

    public void confirmUsersEmail(int id, User token){

        if (isViewAttached()) {
            getView().showLoadingUserHasVerifiedEmail();
        }

        cancelSubscription();
        subscription = api.confirmEmail(id, token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showUserHasVerifiedEmail();
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
                                        getView().showErrorUserHasVerifiedEmail(response.getErrorString());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(Void v) {
                    }
                });
    }

    public boolean validateEmail(EditText inputEmail, Context context) {
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

    public boolean validatePasswords(EditText inputNewPassword, EditText inputNewPasswordConfirm, Context context) {
        boolean valid = true;

        String passNew = inputNewPassword.getText().toString();
        String passNewConfirm = inputNewPasswordConfirm.getText().toString();

        Context mContext = context;
        Resources r = mContext.getResources();

        if (passNew.isEmpty()) {
            inputNewPassword.setError(mContext.getString(R.string.field_cannot_be_empty));
            valid = false;
        } else if (passNewConfirm.isEmpty()) {
            inputNewPasswordConfirm.setError(mContext.getString(R.string.field_cannot_be_empty));
            valid = false;
        } else if (!passNew.equals(passNewConfirm)) {
            inputNewPasswordConfirm.setError(mContext.getString(R.string.new_password_error));
            valid = false;
        }

        return valid;
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
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
    public void attachView(UpdatePasswordAndEmailView view) {
        super.attachView(view);
    }
}
