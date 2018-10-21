package com.tsl.creditcircle.fintech;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tsl.creditcircle.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FinTechQuizFragment extends Fragment {

    public static final String QUIZ_FRAG_TAG = "quizTag";
    @BindView(R.id.aone)
    MaterialButton mAone;
    @BindView(R.id.atwo)
    MaterialButton mAtwo;
    @BindView(R.id.athree)
    MaterialButton mAthree;
    @BindView(R.id.afour)
    MaterialButton mAfour;
    @BindView(R.id.aaone)
    MaterialButton mAaone;
    @BindView(R.id.aatwo)
    MaterialButton mAatwo;
    @BindView(R.id.aathree)
    MaterialButton mAathree;
    @BindView(R.id.aafour)
    MaterialButton mAafour;
    @BindView(R.id.submit_button)
    MaterialButton mSubmitButton;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    Unbinder unbinder;

    public static FinTechQuizFragment newInstance() {
        return new FinTechQuizFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
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

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.aone, R.id.atwo, R.id.athree, R.id.afour, R.id.aaone, R.id.aatwo, R.id.aathree, R.id.aafour, R.id.submit_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aone:
                mAone.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.atwo:
                mAtwo.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.athree:
                mAthree.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.afour:
                mAfour.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.aaone:
                mAaone.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.aatwo:
                mAatwo.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.aathree:
                mAathree.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.aafour:
                mAafour.setBackgroundResource(R.color.colorAccent);
                break;
            case R.id.submit_button:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, QuizFinishFragment.newInstance(), QuizFinishFragment.QUIZ_FINISH_FRAG_TAG)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
