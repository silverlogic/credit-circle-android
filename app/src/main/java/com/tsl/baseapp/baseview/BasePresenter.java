package com.tsl.baseapp.baseview;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.event.BaseEvent;
import com.tsl.baseapp.model.objects.error.Error;
import com.tsl.baseapp.model.objects.user.User;
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
public class BasePresenter extends MvpBasePresenter<BaseView> {

    private Subscription baseSubscriber;
    private EventBus eventBus;

    @Inject
    public BasePresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void doSignUp(User credentials) {

        if (isViewAttached()) {
            getView().showLoading();
        }
//
//        // MAKE THE API CALL HERE
//        cancelSubscription();
//        final BaseApi api = new BaseApiManager().getAppApi();
//        baseSubscriber = api.signUpUser(credentials)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<User>() {
//                    @Override
//                    public void onCompleted() {
//                        // FINISH API CALL
//                        if (isViewAttached()) {
//                            getView().showForm();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //API ERROR
//                        if (isViewAttached()) {
//                            if (isViewAttached()) {
//                                if (e instanceof RetrofitException) {
//                                    RetrofitException error = (RetrofitException) e;
//                                    if (error.getKind() == RetrofitException.Kind.NETWORK) {
//                                        //handle network error
//                                        Timber.d("NETWORK ERROR");
//                                    } else {
//                                        //handle error message from server
//                                        Timber.d(e.getLocalizedMessage());
//                                        Error response = null;
//                                        try {
//                                            response = error.getErrorBodyAs(Error.class);
//                                            String errorString = response.getErrorString();
//                                            Timber.d("Error = " + errorString);
//                                            // FINISH API CALL
//                                            getView().showError(errorString);
//                                        } catch (IOException e1) {
//                                            e1.printStackTrace();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onNext(User user) {
//                        // POST THE RESULT OF API CALL
//                        eventBus.post(new BaseEvent());
//                    }
//                });
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (baseSubscriber != null && !baseSubscriber.isUnsubscribed()) {
            baseSubscriber.unsubscribe();
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override public void attachView(BaseView view) {
        super.attachView(view);
    }
}
