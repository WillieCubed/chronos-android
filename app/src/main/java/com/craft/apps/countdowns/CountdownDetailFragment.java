package com.craft.apps.countdowns;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.craft.apps.countdowns.common.util.CountdownPreconditions;
import com.craft.apps.countdowns.common.viewmodel.CountdownViewModel;

import static com.craft.apps.countdowns.common.util.IntentUtils.ARG_COUNTDOWN_ID;

/**
 * @version 2.0.0
 * @since 1.0.0
 */
public class CountdownDetailFragment extends Fragment {

    private static final String TAG = CountdownDetailFragment.class.getSimpleName();

    private String mCountdownId;

    private BottomSheetBehavior mBehavior;

    public static CountdownDetailFragment newInstance(String countdownId) {
        CountdownDetailFragment fragment = new CountdownDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTDOWN_ID, countdownId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCountdownId = savedInstanceState.getString(ARG_COUNTDOWN_ID);
        } else {
            mCountdownId = CountdownPreconditions.checkValidArgs(getArguments());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBehavior = BottomSheetBehavior.from(view);
        CountdownDetailClickHandler handler = new CountdownDetailClickHandler(view, mCountdownId);
        CountdownViewModel viewModel = ViewModelProviders.of(this)
                .get(CountdownViewModel.class);
        viewModel.getCountdown().observe(this, handler);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ARG_COUNTDOWN_ID, mCountdownId);
        super.onSaveInstanceState(outState);
    }

    public void show() {
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapse() {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
