package com.craft.apps.countdowns;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.craft.apps.countdowns.adapter.CountdownRecyclerAdapter;
import com.craft.apps.countdowns.adapter.CountdownRecyclerAdapter.CountdownSelectionListener;
import com.craft.apps.countdowns.common.database.UserRepository;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;
import com.craft.apps.countdowns.widget.SingleCountdownWidget;

/**
 * A {@link Fragment} subclass that displays a list of {@link Countdown}s
 *
 * This is the backbone of the {@link CountdownListActivity} and {@link SingleCountdownWidget}. Any
 * {@link android.app.Activity} hosting this fragment must implement {@link
 * CountdownSelectionListener} to handle {@link CountdownRecyclerAdapter} selection and load events.
 *
 * @version 1.0.0
 * @see CountdownSelectionListener
 * @since 1.0.0
 */
public class CountdownListFragment extends Fragment implements CountdownSelectionListener {

    /**
     * A required argument to instantiate this fragment
     */
    public static final String ARG_USER_ID = "user_id";

    private static final String TAG = CountdownListFragment.class.getSimpleName();

    private CountdownSelectionListener mListener;

    private ProgressBar mProgressBar;

    private RecyclerView mCountdownList;

    private String mUserId;

    /**
     * Required empty public constructor
     */
    public CountdownListFragment() {
    }

    /**
     * @return A new {@code CountdownListFragment} that will list countdowns with the given user ID
     */
    public static CountdownListFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        CountdownListFragment fragment = new CountdownListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CountdownSelectionListener) {
            mListener = (CountdownSelectionListener) context;
        } else {
            throw new RuntimeException("Context must be an instance of CountdownSelectionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_USER_ID)) {
            mUserId = getArguments().getString(ARG_USER_ID);
        } else {
            throw new IllegalStateException("Fragment must be launched with user_id argument");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown_list, container, false);
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mProgressBar = view.findViewById(R.id.progress_bar);
        mCountdownList = view.findViewById(R.id.list_countdowns);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        User user = UserRepository.fetchUser(mUserId);
        mCountdownList.setAdapter(new CountdownRecyclerAdapter(mListener, this, user));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(mUserId, ARG_USER_ID);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLoad() {
        // Necessary to handle load event but pass through selection
        hideProgressBar();
    }

    @Override
    public void onCountdownSelected(String countdownId) {
        mListener.onCountdownSelected(countdownId);
    }

    @Override
    public void onCountdownLongSelected(String countdownId) {
        mListener.onCountdownLongSelected(countdownId);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
