package com.tsl.creditcircle.loanProgress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.tsl.creditcircle.R;
import com.tsl.creditcircle.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoanAcceptedFragment extends Fragment {


    public static final String LOAN_ACCEPTED_FRAGMENT_TAG = "loanAcceptedTag";
    Unbinder unbinder;
    @BindView(R.id.loan_amount_tv)
    TextView mLoanAmountTv;

    public static LoanAcceptedFragment newInstance() {
        return new LoanAcceptedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_accepted, container, false);
        unbinder = ButterKnife.bind(this, view);
        mLoanAmountTv.setText("$" + Hawk.get(Constants.CURRENT_LOAN, 500));

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

}