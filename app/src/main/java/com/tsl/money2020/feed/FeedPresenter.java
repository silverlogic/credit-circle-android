package com.tsl.money2020.feed;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.money2020.api.BaseApi;
import com.tsl.money2020.model.event.UsersEvent;
import com.tsl.money2020.model.objects.api.PaginatedResponse;
import com.tsl.money2020.model.objects.error.Error;
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.model.objects.user.UserList;
import com.tsl.money2020.utils.RetrofitException;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by kevinlavi on 5/5/16.
 */
public class FeedPresenter extends MvpBasePresenter<FeedView> {
    private Subscription projectsSubscription;
    private EventBus eventBus;
    private BaseApi api;

    @Inject
    public FeedPresenter(EventBus eventBus, BaseApi api) {
        this.eventBus = eventBus;
        this.api = api;
    }

    public void getUserList(String token, int page) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        projectsSubscription = api.getUserList(token, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PaginatedResponse<User>>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showFeed();
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
                                    getView().showError();
                                } else {
                                    //handle error message from server
                                    Timber.d(e.getLocalizedMessage());
                                    Error response = null;
                                    try {
                                        response = error.getErrorBodyAs(Error.class);
                                        String errorString = response.getErrorString();
                                        Timber.d("Error = " + errorString);
                                        // FINISH API CALL
                                        getView().showError();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(PaginatedResponse<User> results) {
                        eventBus.post(new UsersEvent(results));
                    }
                });

    }

    public void updateUserList(String token, int page) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        projectsSubscription = api.getUserList(token, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PaginatedResponse<User>>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().updateFeed();
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
                                        getView().showError();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(PaginatedResponse<User> results) {
                        eventBus.post(new UsersEvent(results));
                    }
                });

    }

    public void searchUser(String token, int page, String query, final boolean update) {
        cancelSubscription();
        projectsSubscription = api.searchUser(token, page, query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserList>() {
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
                                        getView().showError();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(UserList results) {
                        List<User> userList = results.getUserList();
                        if (isViewAttached()) {
                            getView().onSearchResult(update, userList);
                        }
                    }
                });

    }

    /**
     * Cancels any previous callback
     */
    private void cancelSubscription() {
        if (projectsSubscription != null && !projectsSubscription.isUnsubscribed()) {
            projectsSubscription.unsubscribe();
        }
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            cancelSubscription();
        }
    }

    @Override public void attachView(FeedView view) {
        super.attachView(view);
    }
}
