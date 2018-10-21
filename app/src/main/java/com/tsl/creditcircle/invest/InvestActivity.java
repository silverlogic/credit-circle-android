package com.tsl.creditcircle.invest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.tsl.creditcircle.R;
import com.tsl.creditcircle.api.BaseApi;
import com.tsl.creditcircle.api.RetrofitReference;
import com.tsl.creditcircle.inviteVouchers.InviteVouchersFragment;
import com.tsl.creditcircle.model.objects.CreateLoan;
import com.tsl.creditcircle.model.objects.Loan;
import com.tsl.creditcircle.model.objects.Vouch;
import com.tsl.creditcircle.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    public static final String ACCEPT = "ACCEPTED";
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouch);
        ButterKnife.bind(InvestActivity.this);
        setSupportActionBar(mToolbar);
        id = getIntent().getIntExtra("id", 0);
    }

    @OnClick({R.id.vouch_hundred, R.id.vouch_and_invest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.vouch_hundred:
                acceptVouch(new Vouch(ACCEPT, 100, 0));
                break;
            case R.id.vouch_and_invest:
                acceptVouch(new Vouch(ACCEPT, 50, 50));
                break;
        }
    }

    private void acceptVouch(Vouch vouch) {
        BaseApi service = RetrofitReference.getRetrofitInstance().create(BaseApi.class);
        Call<Void> call = service.vouch(Constants.getToken(), id, vouch);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> loanCall, Response<Void> loanResponse) {
                //Timber.d("TESTING loan = " + loanResponse.body().getOriginalAmount());
                Toast.makeText(InvestActivity.this, "Vouch Sent!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
