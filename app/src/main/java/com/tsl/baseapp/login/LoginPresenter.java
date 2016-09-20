package com.tsl.baseapp.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;
import com.tsl.baseapp.model.objects.user.User1;

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

    public void doLogin(User1 credentials) {

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
                    public void onNext(final Token token) {
                        eventBus.post(new LoginSuccessfulEvent(token));
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