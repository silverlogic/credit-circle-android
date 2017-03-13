package com.tsl.baseapp.signup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;
import com.tsl.baseapp.model.event.SignUpSuccessfulEvent;
import com.tsl.baseapp.model.objects.error.Error;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.UpdateUser;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;
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
 * Created by kevinlavi on 5/3/16.
 */
public class SignUpPresenter extends MvpBasePresenter<SignUpView> {

    private Subscription signUpSubscriber;
    private EventBus eventBus;

    @Inject
    public SignUpPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void doSignUp(final User credentials) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
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
                                        signUpSubscriber = api.getCurrentUser(Constants.getToken())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribeOn(Schedulers.io())
                                                .subscribe(new Subscriber<User>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        if (isViewAttached()) {
                                                            getView().signUpSuccessful();
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
                                                    public void onNext(final User user) {
                                                        Hawk.put(Constants.USER, user);
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    public void updateUser(UpdateUser user){

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();

        signUpSubscriber = api.updateUser(Constants.getToken(), user.getId(), user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        // FINISH API CALL
                        if (isViewAttached()) {
                            getView().updateUserSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //API ERROR
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
                                        getView().showError(errorString);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(User user) {
                        // POST THE RESULT OF API CALL
                        Hawk.remove(Constants.USER);
                        Hawk.put(Constants.USER, user);
                    }
                });
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
