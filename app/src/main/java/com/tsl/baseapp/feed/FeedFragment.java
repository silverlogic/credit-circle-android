package com.tsl.baseapp.feed;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.rey.material.widget.SnackBar;
import com.tsl.baseapp.base.BaseApplication;
import com.tsl.baseapp.R;
import com.tsl.baseapp.base.BaseViewStateFragment;
import com.tsl.baseapp.model.event.UsersEvent;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.utils.Constants;
import com.tsl.baseapp.utils.EndlessRecyclerOnScrollListener;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends BaseViewStateFragment<FeedView, FeedPresenter> implements FeedView {


    @Bind(R.id.feed)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    private Context mContext;
    private FeedViewState vs;
    private FeedComponent feedComponent;
    private BaseQuickAdapter mAdapter;
    private List<User> mUserList = new ArrayList<>();
    private EndlessRecyclerOnScrollListener mPagination;
    private int page;
    private final int FIRST_PAGE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        page = FIRST_PAGE;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_feed;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
    }

    @Override
    public ViewState createViewState() {
        return new FeedViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        vs = (FeedViewState) viewState;
        setUpFeed();
    }

    @Override
    public FeedPresenter createPresenter() {
        return feedComponent.presenter();
    }

    @Override
    public void showFeed() {
        vs.setShowFeed();
        stopRefreshing();
        final LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FeedAdapter(mUserList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(recylerOnClick());
        mPagination = new EndlessRecyclerOnScrollListener(lm, mAdapter) {
            @Override
            public void onLoadMore(int current_page) {
                Timber.d(String.valueOf(current_page));
                page = current_page;
                presenter.updateUserList(Constants.getToken(), page);
            }
        };
        mRecyclerView.addOnScrollListener(mPagination);
    }

    @Override
    public void showError() {
        vs.setShowError();
        stopRefreshing();
    }

    @Override
    public void showLoading() {
        vs.setShowLoading();
        if (page == FIRST_PAGE){
            mSwipe.setRefreshing(true);
        }
    }

    @Override
    public void fetchUsers() {
        vs.fetchUsers();
        presenter.getUserList(Constants.getToken(), page);
    }

    @Override
    public void updateFeed() {
        vs.updateFeed();
        stopRefreshing();
    }

    @Override
    protected void injectDependencies() {
        feedComponent = DaggerFeedComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(UsersEvent event){
        List<User> userList = event.getUserList();
        if (mUserList.size() == 0){
            mUserList.addAll(event.getUserList());
        } else {
            // add data - pagination
            mAdapter.addData(userList);
        }
    }

    private void setUpFeed(){
        mSwipe.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = FIRST_PAGE;
                mPagination.reset(page, true);
                mAdapter.getData().clear();
                presenter.getUserList(Constants.getToken(), page);
            }
        });
        fetchUsers();
    }

    private void stopRefreshing(){
        if (mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
    }

    private BaseQuickAdapter.OnRecyclerViewItemClickListener recylerOnClick() {
        BaseQuickAdapter.OnRecyclerViewItemClickListener onClickListener = new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // onclick listener for recyclerview item
                SnackBar.make(mContext)
                        .applyStyle(R.style.SnackBarSingleLine)
                        .text("Selected item #" + String.valueOf(position))
                        .singleLine(true)
                        .duration(3000)
                        .show(getActivity());
            }
        };
        return onClickListener;
    }

}
