package com.tsl.money2020.feed;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.orhanobut.hawk.Hawk;
import com.tsl.money2020.base.BaseApplication;
import com.tsl.money2020.R;
import com.tsl.money2020.base.BaseViewStateFragment;
import com.tsl.money2020.model.event.UsersEvent;
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.tutorial.TutorialActivity;
import com.tsl.money2020.settings.SettingsActivity;
import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.utils.EndlessRecyclerOnScrollListener;
import com.tsl.money2020.utils.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends BaseViewStateFragment<FeedView, FeedPresenter> implements FeedView {


    @BindView(R.id.feed)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    private Context mContext;
    private FeedViewState vs;
    private FeedComponent feedComponent;
    private BaseQuickAdapter mAdapter;
    private List<User> mUserList = new ArrayList<>();
    private EndlessRecyclerOnScrollListener mPagination;
    private int page;
    private final int FIRST_PAGE = 1;
    private LinearLayoutManager lm;
    private MaterialSearchView mSearchView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        page = FIRST_PAGE;
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_feed, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);

        mSearchView.setMenuItem(item);
        mSearchView.setVoiceSearch(false);
        mSearchView.setEllipsize(true);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                if (!Utils.isNetworkAvailable(mContext)){
                    Toast.makeText(mContext, "Search not available while offline", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!query.isEmpty() && !query.equals("")){
                        //clear adapter then search
                        mSwipe.setRefreshing(true);
                        presenter.searchUser(Constants.getToken(), 1, query, false);
                        mPagination = new EndlessRecyclerOnScrollListener(lm, mAdapter) {
                            @Override
                            public void onLoadMore(int current_page) {
                                Timber.d(String.valueOf(current_page));
                                page = current_page;
                                presenter.searchUser(Constants.getToken(), page, query, true);
                            }
                        };
                        mRecyclerView.clearOnScrollListeners();
                        mRecyclerView.addOnScrollListener(mPagination);
                    }
                    else {
                        resetToOriginal();
                    }
                }
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                if (!Utils.isNetworkAvailable(mContext)){
                    Toast.makeText(mContext, "Search not available while offline", Toast.LENGTH_SHORT).show();
                    mSearchView.closeSearch();
                }
            }

            @Override
            public void onSearchViewClosed() {
                resetToOriginal();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Utils.startActivity(getActivity(), SettingsActivity.class, false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_feed;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        boolean hasSeenTutorial = Hawk.get(Constants.VIEWED_TUTORIAL, false);
        if (!hasSeenTutorial) {
            Utils.startActivity(getActivity(), TutorialActivity.class, false);
        }
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
        lm = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FeedAdapter(mUserList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(recylerOnClick());
        mPagination = new EndlessRecyclerOnScrollListener(lm, mAdapter) {
            @Override
            public void onLoadMore(int current_page) {
                Timber.d(String.valueOf(current_page));
                page = current_page;
                presenter.updateUserList(Constants.getToken(), page);
            }
        };
        mRecyclerView.clearOnScrollListeners();
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
        if (Utils.isNetworkAvailable(mContext)){
            presenter.getUserList(Constants.getToken(), page);
        }
        else {
            Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            mUserList = new ArrayList<>(Realm.getDefaultInstance().where(User.class).findAll());
            showFeed();
        }
    }

    @Override
    public void updateFeed() {
        vs.updateFeed();
        stopRefreshing();
    }

    @Override
    public void onSearchResult(boolean update, List<User> userList) {
        vs.searchResult();
        stopRefreshing();
        if (update) mAdapter.addData(userList);
        else mAdapter.setNewData(userList);
    }

    @Override
    protected void injectDependencies() {
        feedComponent = DaggerFeedComponent.builder()
                .baseAppComponent(BaseApplication.getBaseComponents())
                .build();
    }

    @Subscribe
    public void onEvent(UsersEvent event){
        event.getResponse().persist();
        List<User> userList = event.getUserList();
        if (mUserList.size() == 0){
            mUserList.addAll(event.getUserList());
        } else {
            // add data - pagination
            mAdapter.addData(userList);
        }
        for (User user : userList){
            //preload images
            Glide.with(mContext).load(user.getUserImages().getFull_size()).preload();
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
                fetchUsers();
                if (mSearchView.isSearchOpen()){
                    mSearchView.closeSearch();
                }
            }
        });
        fetchUsers();
    }

    private void stopRefreshing(){
        if (mSwipe.isRefreshing()){
            mSwipe.setRefreshing(false);
        }
    }

    private void resetToOriginal() {
        // reset to original list when nothing typed in
        page = FIRST_PAGE;
        fetchUsers();
    }

    private BaseQuickAdapter.OnItemClickListener recylerOnClick(){
        return new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                User user = mUserList.get(position);
//                Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
//                intent.putExtra(UserDetailsFragment.USER_ID, user.getId());
//                intent.putExtra(UserDetailsFragment.IS_CURRENT_USER, false);
//                getActivity().startActivity(intent);
//                getActivity().overridePendingTransition(0, 0);
            }
        };
    }
}
