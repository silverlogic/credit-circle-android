package com.tsl.baseapp.login;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.objects.error.Error;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.RetrofitException;
import com.tsl.baseapp.utils.Writer;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by kevinlavi on 4/26/16.
 */
public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private Subscription loginSubscriber;
    private EventBus eventBus;

    @Inject
    public LoginPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void doLogin(User credentials, final Context context) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
        loginSubscriber = api.loginUser(credentials)
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
                        Hawk.put(Constants.TOKEN, token.getToken());
                        loginSubscriber = api.getCurrentUser(Constants.getToken())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<User>() {
                                    @Override
                                    public void onCompleted() {
                                        if (isViewAttached()) {
                                            getView().loginSuccessful();
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
                                        // persist user id for fetching from realms
                                        Hawk.put(Constants.USER_ID, user.getId());
                                        // persist current user
                                        User persistedUser = Writer.persist(user);
                                    }
                                });
                    }
                });
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (loginSubscriber != null && !loginSubscriber.isUnsubscribed()) {
            loginSubscriber.unsubscribe();
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override public void attachView(LoginView view) {
        super.attachView(view);
    }
}