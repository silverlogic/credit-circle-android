package com.tsl.baseapp.feed;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.event.UsersEvent;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.model.objects.user.UserList;

import org.greenrobot.eventbus.EventBus;
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

    @Inject
    public FeedPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void getUserList(String token, int page) {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
        projectsSubscription = api.getUserList(token, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserList>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().showFeed();
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
                    public void onNext(UserList results) {
                        List<User> userList = results.getUserList();
                        eventBus.post(new UsersEvent(userList));
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
