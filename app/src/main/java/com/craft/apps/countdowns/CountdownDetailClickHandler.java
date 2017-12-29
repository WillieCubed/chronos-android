package com.craft.apps.countdowns;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.craft.apps.countdowns.common.analytics.CountdownAnalytics;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.index.Indexer;
import com.craft.apps.countdowns.util.Users;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

/**
 * An {@link Observer<Countdown>} that handles duplicate functionality for the
 * {@link CountdownDetailFragment} and {@link ModalCountdownBottomSheet}
 *
 * @version 1.0.0
 * @see CountdownDetailFragment
 * @see ModalCountdownBottomSheet
 * @since 2.0.0
 */
public class CountdownDetailClickHandler implements
        Observer<Countdown>,
        Toolbar.OnMenuItemClickListener,
        View.OnClickListener {

    private static final String TAG = CountdownDetailClickHandler.class.getSimpleName();

    // TODO: 12/28/2017 Move views into compound CountdownDisplayView

    private Toolbar mDetailToolbar;

    private ProgressBar mProgressBar;

    private TextView mTitleView;

    private TextView mDescriptionView;

    private TextView mStartText;

    private TextView mFinishText;

    private View mContentView;

    private Context mContext;

    private String mCountdownId;

    public CountdownDetailClickHandler(View view, String countdownId) {
        mContext = view.getContext();
        mCountdownId = countdownId;

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeletionDialog();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_countdown_detail_header:
                break;
        }
        toggleMenuItems();
    }

    public void showMenuItems() {
        MenuItem deleteButton = mDetailToolbar.getMenu().findItem(R.id.action_delete);
        deleteButton.setVisible(true);
    }

    public void hideMenuItems() {
        MenuItem deleteButton = mDetailToolbar.getMenu().findItem(R.id.action_delete);
        deleteButton.setVisible(false);
    }

    private void toggleMenuItems() {

    }

    @Override
    public void onChanged(@Nullable Countdown countdown) {
        if (countdown != null) {
            hideProgressBar();
            showContent();

            Locale locale = UnitsFormatter.getDeviceLocale(mContext);
            updateToolbarText(countdown);

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
    }

    private void updateToolbarText(Countdown countdown) {
        int daysUntil = UnitsFormatter.getUnitsUntil(
                countdown.getFinishTime(), UnitsFormatter.DAYS);
        if (daysUntil <= 0) {
            daysUntil = 0;
        }
        String countText = mContext.getResources()
                .getQuantityString(R.plurals.countdown_unit_days, daysUntil,
                        daysUntil);
        mDetailToolbar.setTitle(countdown.getTitle());
        mDetailToolbar.setSubtitle(countText);
    }

    private void showContent() {
        mContentView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showDeletionDialog() {
        FirebaseUser user = Users.getCurentUser();
        if (user == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.query_dialog_delete_countdown)
                .setMessage(R.string.query_dialog_delete_countdown_details)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                    CountdownManager.deleteCountdown(getContext(), user.getUid(), countdownId);
                    OldDatabase.deleteUserCountdown(mCountdownId, user.getUid(),
                            (databaseError, databaseReference) -> {
                                if (databaseError != null) {
                                    Log.d(TAG, "onComplete: Countdown " + mCountdownId + " deleted");
                                    CountdownAnalytics.getInstance(mContext)
                                            .logDeletion(mCountdownId);
                                    Indexer.removeCountdownIndex(mCountdownId);
                                }
                            });
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
