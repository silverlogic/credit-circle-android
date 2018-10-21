package com.tsl.creditcircle.fintech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.hawk.Hawk;
import com.tsl.creditcircle.R;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tsl.creditcircle.home.HomeFragment;
import com.tsl.creditcircle.utils.Constants;

public class FinTechFragment extends Fragment {

    public static FinTechFragment newInstance() {
        return new FinTechFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fintech, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quick_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.certificate:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, FinTechQuizFragment.newInstance(), FinTechQuizFragment.QUIZ_FRAG_TAG)
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
