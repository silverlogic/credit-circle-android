package com.tsl.creditcircle.fintech;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.hawk.Hawk;
import com.tsl.creditcircle.R;
import com.tsl.creditcircle.loanProgress.LoanProgressFragment;
import com.tsl.creditcircle.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class QuizFinishFragment extends Fragment {

    public static final String QUIZ_FINISH_FRAG_TAG = "quizFinishTag";
    Unbinder unbinder;
    @BindView(R.id.done_button)
    MaterialButton mDoneButton;

    public static QuizFinishFragment newInstance() {
        return new QuizFinishFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish_quiz, container, false);
        int credit = Hawk.get(Constants.CURRENT_CREDIT, 280);
        credit += 30;
        Hawk.put(Constants.CURRENT_CREDIT, credit);
        unbinder = ButterKnife.bind(this, view);
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

    @OnClick(R.id.done_button)
    public void onViewClicked() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, LoanProgressFragment.newInstance(), LoanProgressFragment.LOAN_PROGRESS_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }
}
