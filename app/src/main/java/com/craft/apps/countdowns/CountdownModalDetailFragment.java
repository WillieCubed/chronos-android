package com.craft.apps.countdowns;

import static com.craft.apps.countdowns.common.util.IntentUtils.ARG_COUNTDOWN_ID;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.craft.apps.countdowns.common.analytics.CountdownAnalytics;
import com.craft.apps.countdowns.common.display.CountdownDeletionDialog.CountdownDeletionListener;
import com.craft.apps.countdowns.common.util.CountdownPreconditions;

/**
 * A {@link Fragment} that displays {@link com.craft.apps.countdowns.common.model.Countdown} details
 *
 * It's recommended Use the {@link CountdownModalDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (3/18/17)
 */
public class CountdownModalDetailFragment extends BottomSheetDialogFragment implements
        CountdownDetailDisplay, CountdownDeletionListener {

    private static final String TAG = CountdownModalDetailFragment.class.getSimpleName();

    private DetailSheetController mController;

    /**
     * Required empty public constructor
     */
    public CountdownModalDetailFragment() {
    }

    /**
     * A factory starter method used to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param countdownId A valid countdown ID from the database
     * @return A new instance of fragment CountdownModalDetailFragment.
     */
    public static CountdownModalDetailFragment newInstance(String countdownId) {
        CountdownModalDetailFragment fragment = new CountdownModalDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTDOWN_ID, countdownId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use preconditions here because this should always be launched with arguments
        String countdownId = CountdownPreconditions.checkValidArgs(getArguments());
        mController = new DetailSheetController(getContext(), this, countdownId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mController.init(view);
        mController.startObserving();
    }

    @Override
    public void showDisplay(FragmentManager fragmentManager) {
        Log.d(TAG, "showDisplay: Showing bottom sheet");
        show(fragmentManager, "CountdownDetailFragment");
    }

    @Override
    public void collapseDisplay() {
        Log.v(TAG, "collapseDisplay: Dismissing bottom sheet");
        dismiss();
    }

    @Override
    public void dismissDisplay() {
        Log.v(TAG, "dismissDisplay: Dismissing bottom sheet");
        dismiss();
    }

    @Override
    public void toggleDisplay() {
        // no-op since this is a modal fragment
    }

    @Override
    public String getCountdownId() {
        return mController.getCountdownReference().getKey();
    }

    @Override
    public void setCountdownId(String countdownId) {
        mController.updateCountdown(countdownId);
    }

    @Override
    public void onDeletion(String countdownId) {
        Log.d(TAG, "onDelete: Countdown " + countdownId + " deleted");
        CountdownAnalytics.getInstance(getContext())
                .logDeletion(countdownId);
        dismissDisplay();
    }
}
