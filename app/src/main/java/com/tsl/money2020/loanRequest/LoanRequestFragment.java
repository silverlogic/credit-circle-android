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
import com.orhanobut.hawk.Hawk;
import com.tsl.money2020.R;
import com.tsl.money2020.api.BaseApi;
import com.tsl.money2020.api.RetrofitReference;
import com.tsl.money2020.inviteVouchers.InviteVouchersFragment;
import com.tsl.money2020.model.objects.CreateLoan;
import com.tsl.money2020.model.objects.Friend;
import com.tsl.money2020.model.objects.Loan;
import com.tsl.money2020.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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
    private int currentValue;

    public static LoanRequestFragment newInstance() {
        return new LoanRequestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_request, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRulerPicker.selectValue(250);
        mRulerPicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {
            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {
                currentValue = selectedValue;
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
                createLoanRequest();
                break;
        }
    }

    private void createLoanRequest(){
        BaseApi service = RetrofitReference.getRetrofitInstance().create(BaseApi.class);
        CreateLoan createLoan = new CreateLoan();
        createLoan.setOriginalAmount(currentValue);
        Call<Loan> call = service.createLoan(Constants.getToken(), createLoan);
        call.enqueue(new Callback<Loan>() {
            @Override
            public void onResponse(Call<Loan> loanCall, Response<Loan> loanResponse) {
                Hawk.put(Constants.CURRENT_LOAN, loanResponse.body());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, InviteVouchersFragment.newInstance(), InviteVouchersFragment.INVITE_VOUCHERS_TAG)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onFailure(Call<Loan> call, Throwable t) {
            }
        });
    }
}