package com.craft.apps.countdowns;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.craft.apps.countdowns.common.util.CountdownPreconditions;
import com.craft.apps.countdowns.common.viewmodel.CountdownViewModel;

import java.util.Objects;

import static com.craft.apps.countdowns.common.util.IntentUtils.ARG_COUNTDOWN_ID;

/**
 * @version 1.0.0
 * @since 2.0.0
 */
public class ModalCountdownBottomSheet extends BottomSheetDialogFragment {

    private String mCountdownId;

    /**
     * Required empty public constructor.
     */
    public ModalCountdownBottomSheet() {
    }

    /**
     * Creates a new ModalCountdownBottomSheet instance that displays countdown
     * details for the given countdown ID.
     */
    public static ModalCountdownBottomSheet newInstance(String countdownId) {
        ModalCountdownBottomSheet fragment = new ModalCountdownBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTDOWN_ID, countdownId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getArguments(), "countdown_id must be supplied as argument");
        if (savedInstanceState != null) {
            mCountdownId = savedInstanceState.getString(ARG_COUNTDOWN_ID);
        }
        mCountdownId = CountdownPreconditions.checkValidArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CountdownDetailClickHandler handler = new CountdownDetailClickHandler(view, mCountdownId);
        CountdownViewModel viewModel = ViewModelProviders.of(this)
                .get(CountdownViewModel.class);
        viewModel.getCountdown().observe(this, handler);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_COUNTDOWN_ID, mCountdownId);
        super.onSaveInstanceState(outState);
    }
}
