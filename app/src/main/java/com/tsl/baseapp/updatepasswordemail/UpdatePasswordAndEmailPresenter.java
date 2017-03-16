package com.tsl.baseapp.updatepasswordemail;

import android.content.Context;

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

    @Inject
    public UpdatePasswordAndEmailPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void updateEmail(String token, User credentials, final Context context) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
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

    public void changePassword(String token, User creds, final Context context){

        if (isViewAttached()) {
            getView().showLoading();
        }

        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
        subscription = api.changePassword(token, creds)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showChangePasswordSuccess();
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
