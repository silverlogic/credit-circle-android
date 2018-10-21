package com.tsl.money2020.loanRequest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;
import com.tsl.money2020.R;
import com.tsl.money2020.inviteVouchers.InviteVouchersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoanRequestFragment extends Fragment {

    @BindView(R.id.ruler_picker)
    RulerValuePicker mRulerPicker;
    @BindView(R.id.picker_amount)
    TextView mPickerAmount;
    Unbinder unbinder;
    @BindView(R.id.fintech_button)
    FloatingActionButton mFintechButton;
    @BindView(R.id.invite_button)
    FloatingActionButton mInviteButton;

    public static LoanRequestFragment newInstance() {
        return new LoanRequestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_request, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRulerPicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {
                mPickerAmount.setText("$" + selectedValue);
            }
        });
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

    @OnClick({R.id.fintech_button, R.id.invite_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fintech_button:
                break;
            case R.id.invite_button:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, InviteVouchersFragment.newInstance(), InviteVouchersFragment.INVITE_VOUCHERS_TAG)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}