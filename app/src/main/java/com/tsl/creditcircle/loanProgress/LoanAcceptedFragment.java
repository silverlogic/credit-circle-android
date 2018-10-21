package com.tsl.creditcircle.loanProgress;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;
import com.orhanobut.hawk.Hawk;
import com.tsl.creditcircle.R;
import com.tsl.creditcircle.model.event.VouchEvent;
import com.tsl.creditcircle.model.objects.Friend;
import com.tsl.creditcircle.model.objects.Loan;
import com.tsl.creditcircle.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoanAcceptedFragment extends Fragment {

    @BindView(R.id.ruler_picker)
    RulerValuePicker mRulerPicker;
    @BindView(R.id.picker_amount)
    TextView mPickerAmount;
    Unbinder unbinder;
    @BindView(R.id.repayment_amount)
    TextView mRepaymentAmount;
    @BindView(R.id.vouched_recyclerView)
    RecyclerView mVouchedRecyclerView;
    @BindView(R.id.submit_button)
    MaterialButton mSubmitButton;
    @BindView(R.id.approved_amount)
    TextView mApprovedAmount;
    private int currentValue;
    public static final String LOAN_PROGRESS_FRAGMENT_TAG = "loanProgressTag";
    private int mVouchedByFriends;
    private int mFundedByFriends;
    private List<Friend> mFriendList;
    private BaseQuickAdapter mAdapter;

    public static LoanAcceptedFragment newInstance() {
        return new LoanAcceptedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_progress, container, false);
        unbinder = ButterKnife.bind(this, view);

        mFriendList = new ArrayList<>();
        setUpRecyclerView();
        Loan loan = Hawk.get(Constants.CURRENT_LOAN);
        mPickerAmount.setText("$" + loan.getOriginalAmount());
        currentValue = loan.getOriginalAmount();
        mRulerPicker.selectValue(currentValue);
        mRulerPicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
                int remainder = selectedValue % 5;
                int newNum = selectedValue - remainder;
                if (selectedValue % 5 > 2) {
                    newNum += 5;
                }
                mRulerPicker.selectValue(newNum);
                currentValue = newNum;
                mPickerAmount.setText("$" + currentValue);
                int repayment = (int) (currentValue * 1.2);
                mRepaymentAmount.setText("$" + repayment);
                setSubmitEnabled();
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {
            }
        });
        mFundedByFriends = 0;
        mVouchedByFriends = 0;
        updateApprovedFor();
        setSubmitEnabled();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.submit_button, R.id.invite_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_button:
                break;
            case R.id.invite_button:
                break;
        }
    }

    private void updateApprovedFor(){
        mApprovedAmount.setText("Approved for $" + getApprovedFor());
    }

    private int getApprovedFor(){
        return Hawk.get(Constants.CURRENT_CREDIT, 280) + mFundedByFriends + mVouchedByFriends;
    }

    @Subscribe
    public void onEvent(final VouchEvent event){
        mFriendList.add(event.getFriend());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mVouchedByFriends += event.getVouchAmount();
                mFundedByFriends += event.getInvestAmount();
                updateApprovedFor();
                setSubmitEnabled();
            }
        });
    }

    private void setUpRecyclerView() {
        mAdapter = new LoanProgressAdapter(mFriendList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mVouchedRecyclerView.setLayoutManager(layoutManager);
        mVouchedRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("ResourceAsColor")
    private void setSubmitEnabled(){
        boolean enabled = getApprovedFor() >= currentValue;
        mSubmitButton.setEnabled(enabled);
        if (!enabled){
            mSubmitButton.setText("");
        }
        else {
            mSubmitButton.setText("SUBMIT");
        }
    }
}