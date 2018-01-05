package com.craft.apps.countdowns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.craft.apps.countdowns.common.util.CountdownPreconditions;

// TODO: 6/30/17 Find proper use for this

/**
 * @author willie
 * @version 1.0.0
 * @since 6/25/17
 */
public class CountdownDetailFragment extends Fragment {

    private static final String TAG = CountdownDetailFragment.class.getSimpleName();

    /**
     * Used to handle functionality common to this and {@link CountdownModalDetailFragment}
     */
    private DetailSheetController mController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String countdownId = CountdownPreconditions.checkValidArgs(getArguments());
        mController = new DetailSheetController(getContext(), null, countdownId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mController.init(view);
        mController.startObserving();
    }
}
