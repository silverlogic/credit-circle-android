package com.tsl.baseapp.baseview;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.model.objects.user.User;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

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
//                            Timber.d(e.getMessage());
//                            getView().showError();
//                        }
//                    }
//
//                    @Override
//                    public void onNext(User user) {
//                        // POST THE RESULT OF API CALL
//                        eventBus.post(new SignUpSuccessfulEvent(user));
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
