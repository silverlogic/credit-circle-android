package com.tsl.baseapp.feed;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.hawk.Hawk;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.api.BaseApiManager;
import com.tsl.baseapp.model.Objects.project.Project;
import com.tsl.baseapp.model.Objects.project.ProjectsResults;
import com.tsl.baseapp.model.Utilities.Constants;
import com.tsl.baseapp.model.event.ProjectsEvent;

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

    public void getProjects() {

        if (isViewAttached()) {
            getView().showLoading();
        }

        // Kind of "callback"
        cancelSubscription();
        final BaseApi api = new BaseApiManager().getAppApi();
        String token = Hawk.get(Constants.TOKEN);
        projectsSubscription = api.getProjects(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ProjectsResults>() {
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
                    public void onNext(ProjectsResults results) {
                        List<Project> projects = results.getProjects();
                        eventBus.post(new ProjectsEvent(projects));
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
