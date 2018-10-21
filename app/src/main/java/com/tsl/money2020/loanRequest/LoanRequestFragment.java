package com.tsl.money2020.loanRequest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;
import com.tsl.money2020.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LoanRequestFragment extends Fragment {

    @BindView(R.id.ruler_picker)
    RulerValuePicker mRulerPicker;
    @BindView(R.id.picker_amount)
    TextView mPickerAmount;
    Unbinder unbinder;

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
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}