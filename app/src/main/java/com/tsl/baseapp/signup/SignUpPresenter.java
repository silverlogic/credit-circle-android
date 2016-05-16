package com.tsl.baseapp.signup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.Objects.token.Token;
import com.tsl.baseapp.model.Objects.user.AuthCredentials;
import com.tsl.baseapp.model.Objects.user.SignUpCredentials;
import com.tsl.baseapp.model.Objects.user.User;
import com.tsl.baseapp.model.event.SignUpSuccessfulEvent;

import org.greenrobot.eventbus.EventBus;

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

    public void doSignUp(SignUpCredentials credentials) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        BaseApi api = new BaseApiManager().getAppApi();
        signUpSubscriber = api.signUpUser(credentials)
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
                            Timber.d(e.getMessage());
                            getView().showError();
                        }
                    }

                    @Override
                    public void onNext(User user) {
                        eventBus.post(new SignUpSuccessfulEvent(user));
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
