package com.tsl.baseapp.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.model.Objects.user.UserManager;
import com.tsl.baseapp.model.Objects.user.AuthCredentials;
import com.tsl.baseapp.model.Objects.user.User;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kevinlavi on 4/26/16.
 */
public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private UserManager userManager;
    private Subscriber<User> subscriber;
    private EventBus eventBus;

    @Inject
    public LoginPresenter(UserManager userManager, EventBus eventBus) {
        this.userManager = userManager;
        this.eventBus = eventBus;
    }

    public void doLogin(AuthCredentials credentials) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        subscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {
                if (isViewAttached()) {
                    getView().loginSuccessful();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showError();
                }
            }

            @Override
            public void onNext(User user) {
                eventBus.post(new LoginSuccessfulEvent(user));

            }
        };

        // do the login
        userManager.doLogin(credentials)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
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