package com.tsl.baseapp.feed;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tsl.baseapp.BaseApplication;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.Objects.project.Project;
import com.tsl.baseapp.model.event.ProjectsEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends BaseViewStateFragment<FeedView, FeedPresenter> implements FeedView {


    @Bind(R.id.feed)
    RecyclerView mFeed;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    private Context mContext;
    private FeedViewState vs;
    private FeedComponent feedComponent;
    private FeedAdapter mFeedAdapter;
    private List<Project> mProjectsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_feed;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        setUpFeed();
    }

    @Override
    public ViewState createViewState() {
        return new FeedViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (FeedViewState) viewState;
        fetchProjects();
    }

    @Override
    public FeedPresenter createPresenter() {
        return feedComponent.presenter();
    }

    @Override
    public void showFeed() {
        vs.setShowFeed();
        stopRefreshing();
    }

    @Override
    public void showError() {
        vs.setShowError();
        stopRefreshing();
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();
    }

    @Override
    public void fetchProjects() {
        presenter.getProjects();
        mSwipe.setRefreshing(true);
    }

    @Override
    protected void injectDependencies() {
        feedComponent = DaggerFeedComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(ProjectsEvent event){
        mProjectsList = event.getProjects();
        mFeedAdapter.setNewsList(mProjectsList);
    }

    private void setUpFeed(){
        mFeedAdapter = new FeedAdapter(mContext);
        mFeed.setLayoutManager(new LinearLayoutManager(mContext));
        mFeed.setAdapter(mFeedAdapter);

        mSwipe.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchProjects();
            }
        });
    }

    private void stopRefreshing(){
        if (mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
    }

}
