package com.tsl.baseapp.userdetails;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.R;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.event.LoginSuccessfulEvent;
import com.tsl.baseapp.model.objects.error.Error;
import com.tsl.baseapp.model.objects.user.UpdateUser;
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
 * Created by kevinlavi on 5/6/16.
 */
public class UserDetailsPresenter extends MvpBasePresenter<UserDetailsView> {

    private Subscription userDetailsSubscriber;
    private EventBus eventBus;

    @Inject
    public UserDetailsPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void fetchUser(final Context context) {

        if (isViewAttached()) {
            getView().showLoading(context.getString(R.string.fetching_user));
        }
        // MAKE THE API CALL HERE
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
        userDetailsSubscriber = api.getCurrentUser(Constants.getToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().fetchUserSuccess();
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
                        //persist user
                        User persistUser = Writer.persist(user);
                    }
                });
    }

    public void updateUser(final Context context, UpdateUser user) {

        if (isViewAttached()) {
            getView().showLoading(context.getString(R.string.updating_user));
        }
        // MAKE THE API CALL HERE
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
        userDetailsSubscriber = api.updateUser(Constants.getToken(), user.getId(), user)
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
                        // persist user
                        User persistUser = Writer.persist(user);
                    }
                });
    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (userDetailsSubscriber != null && !userDetailsSubscriber.isUnsubscribed()) {
            userDetailsSubscriber.unsubscribe();
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override public void attachView(UserDetailsView view) {
        super.attachView(view);
    }
}
