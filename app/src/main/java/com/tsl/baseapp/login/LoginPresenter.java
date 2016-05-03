package com.tsl.baseapp.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.Api.ApiManager;
import com.tsl.baseapp.model.Api.AppApi;
import com.tsl.baseapp.model.Objects.token.Token;
import com.tsl.baseapp.model.Objects.user.AuthCredentials;
import com.tsl.baseapp.model.Objects.user.User;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;

import org.greenrobot.eventbus.EventBus;

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

    public void doLogin(AuthCredentials credentials) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        BaseApi api = new BaseApiManager().getAppApi();
        loginSubscriber = api.loginUser(credentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Token>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().loginSuccessful();
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
                    public void onNext(Token token) {
                        String t = token.getToken();
                        eventBus.post(new LoginSuccessfulEvent(t));
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