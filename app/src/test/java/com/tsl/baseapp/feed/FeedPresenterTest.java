package com.tsl.baseapp.feed;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.RxSchedulersOverrideRule;
import com.tsl.baseapp.api.BaseApi;
import com.tsl.baseapp.model.objects.api.PaginatedResponse;
import com.tsl.baseapp.model.objects.token.Token;
import com.tsl.baseapp.model.objects.user.User;
import com.tsl.baseapp.model.objects.user.UserImages;
import com.tsl.baseapp.model.objects.user.UserList;

import org.greenrobot.eventbus.EventBus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Kevin Lavi on 3/31/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FeedPresenterTest {
    private FeedPresenter presenter;
    @Mock
    private FeedView view;
    @Mock
    Context mMockContext;
    @Mock
    private EventBus bus;
    @Mock
    BaseApi mApi;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    Token token;
    private FeedActivity mFeedActivity;
    private FeedFragment mFeedFragment;
    private Resources res;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new FeedPresenter(bus, mApi);
        presenter.attachView(view);
        token = new Token();
        token.setToken("Token 1234567890");

        mFeedActivity = Robolectric.setupActivity(FeedActivity.class);
        mFeedFragment = new FeedFragment();
        SupportFragmentTestUtil.startVisibleFragment(mFeedFragment);
        res = mFeedActivity.getResources();

        mRecyclerView = mFeedFragment.mRecyclerView;
    }

    @Test
    public void getUserList() throws Exception {
        PaginatedResponse<User> results = new PaginatedResponse<>();

        when(mApi.getUserList(any(String.class), any(int.class))).thenReturn(Observable.just(results));

        presenter.getUserList(token.getToken(), 1);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).showFeed();

        mAdapter = new FeedAdapter(getUsers());
        Assert.assertEquals(mAdapter.getItemCount(), 5);
    }

    @Test
    public void updateUserList() throws Exception {
        PaginatedResponse<User> results = new PaginatedResponse<>();

        when(mApi.getUserList(any(String.class), any(int.class))).thenReturn(Observable.just(results));

        presenter.updateUserList(token.getToken(), 1);
        Mockito.verify(view).showLoading();
        Mockito.verify(view).updateFeed();

        mAdapter = new FeedAdapter(getUsers());
        Assert.assertEquals(mAdapter.getItemCount(), 5);
    }

    @Test
    public void searchUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFirst_name("kevin");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);

        UserList userList = new UserList();
        userList.setUserList(users);

        when(mApi.searchUser(any(String.class), any(int.class), Mockito.eq("Kevin"))).thenReturn(Observable.just(userList));

        presenter.searchUser(token.getToken(), 1, "Kevin", true);
        Mockito.verify(view).onSearchResult(true, users);
    }

    private List<User> getUsers(){
        List<User> userList = new ArrayList<>();
        UserImages userImages = new UserImages();
        userImages.setFull_size("https://www.gravatar.com/avatar/dfecf7ab1ec961d5757e76b606e2ac32/?d=retro&s=1024");
        userImages.setSmall("https://www.gravatar.com/avatar/dfecf7ab1ec961d5757e76b606e2ac32/?d=retro&s=64");

        userList.add(new User().testUser("kevin", "lavi", userImages));
        userList.add(new User().testUser("test", "user", userImages));
        userList.add(new User().testUser("another", "one", userImages));
        userList.add(new User().testUser("d", "h", userImages));
        userList.add(new User().testUser("k", "l", userImages));

        return userList;
    }

}
