package com.tsl.money2020.inviteVouchers;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tsl.money2020.R;
import com.tsl.money2020.api.BaseApi;
import com.tsl.money2020.api.BaseApiManager;
import com.tsl.money2020.api.RetrofitReference;
import com.tsl.money2020.model.event.BaseEvent;
import com.tsl.money2020.model.objects.Friend;
import com.tsl.money2020.model.objects.user.User;
import com.tsl.money2020.utils.Constants;
import com.tsl.money2020.utils.RetrofitException;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteVouchersFragment extends Fragment {

    @BindView(R.id.email_radio)
    AppCompatRadioButton mEmailRadio;
    @BindView(R.id.phone_radio)
    AppCompatRadioButton mPhoneRadio;
    @BindView(R.id.friends_recyclerView)
    RecyclerView mFriendsRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.done_button)
    MaterialButton mDoneButton;
    private BaseQuickAdapter mAdapter;
    private boolean inviteByEmailSelected = false;
    private boolean inviteByPhoneSelected = false;
    private List<Friend> mFriendsList;

    public static final String INVITE_VOUCHERS_TAG = "inviteVouchersTag";

    public static InviteVouchersFragment newInstance() {
        return new InviteVouchersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_vouchers, container, false);
        unbinder = ButterKnife.bind(this, view);
        fetchUsers();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.email_radio, R.id.phone_radio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.email_radio:
                if (inviteByPhoneSelected){
                    inviteByPhoneSelected = false;
                    mPhoneRadio.setChecked(false);
                }
                inviteByEmailSelected = true;
                mEmailRadio.setChecked(true);
                break;
            case R.id.phone_radio:
                if (inviteByEmailSelected){
                    inviteByEmailSelected = false;
                    mEmailRadio.setChecked(false);
                }
                inviteByPhoneSelected = true;
                mPhoneRadio.setChecked(true);
                break;
            case R.id.done_button:
                Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void fetchUsers(){
        BaseApi service = RetrofitReference.getRetrofitInstance().create(BaseApi.class);
        Call<List<Friend>> call = service.getUserList(Constants.getToken());
        call.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> friendCall, Response<List<Friend>> friendList) {
                setUpRecyclerView(friendList.body());
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
            }
        });
    }

    private void setUpRecyclerView(List<Friend> body) {
        mFriendsList = body;
        mAdapter = new InviteAdapter(mFriendsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFriendsRecyclerView.setLayoutManager(layoutManager);
        mFriendsRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mDoneButton.setVisibility(View.VISIBLE);
                mFriendsList.get(position).setInvited(true);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}