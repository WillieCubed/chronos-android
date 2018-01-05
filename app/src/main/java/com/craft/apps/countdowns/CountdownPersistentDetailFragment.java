package com.craft.apps.countdowns;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetBehavior.State;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.util.CountdownPreconditions;
import com.craft.apps.countdowns.common.util.Preconditions;

import static com.craft.apps.countdowns.common.util.IntentUtils.ARG_COUNTDOWN_ID;

/**
 * @version 1.0.0
 * @since 1.0.0
 * @deprecated Use CountdownDetailFragment instead
 */
@Deprecated
public class CountdownPersistentDetailFragment extends Fragment implements
        CountdownDetailDisplay {

    private static final String TAG = CountdownPersistentDetailFragment.class.getSimpleName();

    private BottomSheetBehavior mBottomSheetBehavior;

    private String mCountdownId;

    /**
     * Used to handle functionality common to this and {@link CountdownModalDetailFragment}
     */
    private DetailSheetController mController;

    /**
     * A required public empty constructor
     */
    public CountdownPersistentDetailFragment() {
    }

    /**
     * @return A new fragment instance
     */
    @NonNull
    public static CountdownPersistentDetailFragment newInstance() {
        return new CountdownPersistentDetailFragment();
    }

    /**
     * Creates a new fragment instance populated with data from the given {@link Countdown} ID
     */
    public static CountdownPersistentDetailFragment newInstance(String countdownId) {
        Bundle args = new Bundle();
        args.putString(ARG_COUNTDOWN_ID, countdownId);
        CountdownPersistentDetailFragment fragment = new CountdownPersistentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCountdownId = CountdownPreconditions.ifAvailable(getArguments());
        mController = new DetailSheetController(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO: 6/30/17 Find better work around for this
        CoordinatorLayout parent = getActivity().findViewById(R.id.coordinator_root);
//        View parent = ((ViewGroup) getActivity().getWindow().getDecorView().getRootView())
//                .getChildAt(1);
        mBottomSheetBehavior = BottomSheetBehavior.from(parent.getChildAt(2));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.v(TAG, "onStateChanged: State expanded");
                        mController.showMenuItems();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.v(TAG, "onStateChanged: State hidden/collapsed");
                        mController.hideMenuItems();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                // no-op
            }
        });
        mController.init(view);
        if (mCountdownId == null) {
            Log.w(TAG, "onViewCreated: Countdown ID is null, aborting");
            return;
        }
        Log.d(TAG, "onViewCreated: bottom sheet callback created");
        mController.updateCountdown(mCountdownId);
        mController.startObserving();
    }

    @Override
    public void showDisplay(FragmentManager fragmentManager) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void collapseDisplay() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void dismissDisplay() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void toggleDisplay() {
        @State int state = mBottomSheetBehavior.getState();
        switch (state) {
            case BottomSheetBehavior.STATE_HIDDEN:
            case BottomSheetBehavior.STATE_COLLAPSED:
                showDisplay(getActivity().getSupportFragmentManager());
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
            case BottomSheetBehavior.STATE_SETTLING:
                collapseDisplay();
                break;
        }
    }

    @Override
    public String getCountdownId() {
        return mCountdownId;
    }

    @Override
    public void setCountdownId(String countdownId) {
        mCountdownId = Preconditions.checkNotNull(countdownId);
        mController.updateCountdown(countdownId);
    }
}
