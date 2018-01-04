package com.craft.apps.countdowns;

import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.craft.apps.countdowns.common.analytics.CountdownAnalytics;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.index.Indexer;
import com.craft.apps.countdowns.util.Users;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

/**
 * A UI controller class that monitors and displays data.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @deprecated Use {@link ModalCountdownBottomSheet} or {@link CountdownDetailFragment} instead
 */
@Deprecated
public class DetailSheetController implements ValueEventListener, OnClickListener,
        OnMenuItemClickListener {

    private static final String TAG = DetailSheetController.class.getSimpleName();
    private CountdownDetailDisplay mDisplay;

    /**
     * Really only used to fetch strings
     */
    private Context mContext;

    private DatabaseReference mSelectedCountdownReference = null;

    private Toolbar mDetailToolbar;

    private ProgressBar mProgressBar;

    private View mContentView;

    private TextView mTitleView;

    private TextView mDescriptionView;

    private TextView mStartText;

    private TextView mFinishText;

    private boolean mIsSheetShowing = false;

    /**
     * Creates a new controller
     *
     * DetailSheetControllers created with this are not initialized with a countdown ID to load
     * data. You must use {@link #updateCountdown(String)} before {@link #startObserving()} is
     * called.
     */
    @Deprecated
    public DetailSheetController(Context context, CountdownDetailDisplay display) {
        this(context, display, null);
    }

    /**
     * Creates a new controller
     *
     * @param countdownId A valid {@linkplain Countdown} database ID
     */
    @Deprecated
    public DetailSheetController(Context context, CountdownDetailDisplay display,
                                 String countdownId) {
        mContext = context;
        mDisplay = display;
        if (countdownId != null) {
            mSelectedCountdownReference = OldDatabase.getCountdownReference(countdownId);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation hides the progress bar and shows the content
     * populated with data from the {@link Countdown}
     *
     * @param dataSnapshot A snapshot of data provided by the database
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        hideProgressBar();
        showContent();

        Countdown countdown = dataSnapshot.getValue(Countdown.class);
        if (countdown == null) {
            return;
        }
        Locale locale = UnitsFormatter.getDeviceLocale(mContext);
//        UnitsFormatter formatter = UnitsFormatter.creator(CountdownListActivity.this)
//                .startingAt(countdown.getStartTime())
//                .endingAt(countdown.getFinishTime())
//
//        String countAmount = getString(R.string.content_countdown_detail_amount,
//                formatter.as(UnitsFormatter.DAYS), UnitsFormatter.DAYS);
        int daysUntil = UnitsFormatter.getUnitsUntil(
                countdown.getFinishTime(), UnitsFormatter.DAYS);
        if (daysUntil <= 0) {
            daysUntil = 0;
        }
        // TODO: 6/23/17 Fix me for 1.1.0
        String countText = mContext.getResources()
                .getQuantityString(R.plurals.countdown_unit_days, daysUntil,
                        daysUntil);
        mDetailToolbar.setTitle(countdown.getTitle());
        mDetailToolbar.setSubtitle(countText);
        mDetailToolbar.setOnClickListener(this);

        mTitleView.setText(mContext.getString(R.string.content_countdown_detail_name,
                countdown.getTitle()));
        if (countdown.getDescription() != null) {
            mDescriptionView.setText(
                    mContext.getString(R.string.content_countdown_detail_description,
                            countdown.getDescription()));
        } else {
            mDescriptionView.setVisibility(View.GONE);
        }
        String start = UnitsFormatter.withStart(countdown.getStartTime())
                .asDate(locale);
        String end = UnitsFormatter.withEnd(countdown.getFinishTime())
                .asDate(locale);
        mStartText.setText(
                mContext.getString(R.string.content_countdown_detail_start, start));
        mFinishText.setText(
                mContext.getString(R.string.content_countdown_detail_finish, end));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "onCancelled: Error when fetching countdown detail data",
                databaseError.toException());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_countdown_detail_header:
                if (mDisplay instanceof BottomSheetDialogFragment) {
                    mDisplay.dismissDisplay();
                } else {
                    mDisplay.toggleDisplay();
                }
                toggleMenuItems();
                mIsSheetShowing = !mIsSheetShowing;
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_delete:
                showDeletionDialog(mContext);
                break;
        }
        return true;
    }

    /**
     * @param view
     */
    @Deprecated
    public void init(View view) {
        mDetailToolbar = view.findViewById(R.id.toolbar_countdown_detail_header);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mContentView = view.findViewById(R.id.countdown_content_detail);
        mTitleView = view.findViewById(R.id.countdown_title_detail);
        mDescriptionView = view.findViewById(R.id.countdown_description_detail);
        mStartText = view.findViewById(R.id.countdown_start_date_detail);
        mFinishText = view.findViewById(R.id.countdown_finish_date_detail);
        mDetailToolbar.inflateMenu(R.menu.countdown_detail);
        mDetailToolbar.setOnClickListener(this);
        mDetailToolbar.setOnMenuItemClickListener(this);
    }

    /**
     * Begins observing {@linkplain OldDatabase} events.
     */
    @Deprecated
    public void startObserving() {
        mSelectedCountdownReference.addValueEventListener(this);
    }

    /**
     * Notifies the controller to update the data.
     *
     * @param countdownId The database key for a {@link Countdown}
     */
    @Deprecated
    public void updateCountdown(String countdownId) {
        if (mSelectedCountdownReference != null) {
            mSelectedCountdownReference.removeEventListener(this);
        }
        showProgressBar();
        hideContent();
        // Will re-display the data
        mSelectedCountdownReference = OldDatabase.getCountdownReference(countdownId);
        mSelectedCountdownReference.addValueEventListener(this);
    }

    private void showDeletionDialog(Context context) {
        FirebaseUser user = Users.getCurentUser();
        if (user == null) {
            return;
        }
        new Builder(context)
                .setTitle(R.string.query_dialog_delete_countdown)
                .setMessage(R.string.query_dialog_delete_countdown_details)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    final String countdownId = getCountdownReference().getKey();
                    // TODO: 6/30/17 Fix this shite with a custom callback
//                    CountdownRepository.deleteCountdown(getContext(), user.getUid(), countdownId);
                    OldDatabase.deleteUserCountdown(countdownId, user.getUid(),
                            (databaseError, databaseReference) -> {
                                if (databaseError != null) {
                                    Log.d(TAG, "onComplete: Countdown " + countdownId + " deleted");
                                    CountdownAnalytics.getInstance(context)
                                            .logDeletion(countdownId);
                                    Indexer.removeCountdownIndex(countdownId);

                                    mDisplay.collapseDisplay();
                                    hideMenuItems();
                                    mIsSheetShowing = false;
                                }
                            });
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel())
                .show();
    }

    @Deprecated
    public void showMenuItems() {
        MenuItem deleteButton = mDetailToolbar.getMenu().findItem(R.id.action_delete);
        deleteButton.setVisible(true);
    }

    @Deprecated
    public void hideMenuItems() {
        MenuItem deleteButton = mDetailToolbar.getMenu().findItem(R.id.action_delete);
        deleteButton.setVisible(false);
    }

    private void toggleMenuItems() {
        if (mIsSheetShowing) {
            hideMenuItems();
        } else {
            showMenuItems();
        }
    }

    private void hideContent() {
        mContentView.setVisibility(View.GONE);
        mIsSheetShowing = false;
        hideMenuItems();
    }

    private void showProgressBar() {
        mContentView.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        mContentView.setVisibility(View.VISIBLE);
        mIsSheetShowing = true;
        showMenuItems();
    }

    /**
     * Hides the loading spinner
     */
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Deprecated
    protected DatabaseReference getCountdownReference() {
        return mSelectedCountdownReference;
    }
}
