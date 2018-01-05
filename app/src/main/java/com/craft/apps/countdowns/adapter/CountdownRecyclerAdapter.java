package com.craft.apps.countdowns.adapter;

import static com.craft.apps.countdowns.common.model.SortOptions.COUNTDOWN_LENGTH;
import static com.craft.apps.countdowns.common.model.SortOptions.DATE_CREATED;
import static com.craft.apps.countdowns.common.model.SortOptions.TIME_LEFT;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.adapter.CountdownRecyclerAdapter.ViewHolder;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.SortOptions;
import com.craft.apps.countdowns.common.model.SortOptions.SortOption;
import com.craft.apps.countdowns.common.settings.Preferences;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author willie
 * @version 1.0.0
 * @since 3/18/17
 */
public class CountdownRecyclerAdapter extends FirebaseIndexRecyclerAdapter<Countdown, ViewHolder> {

    private static final String TAG = CountdownRecyclerAdapter.class.getSimpleName();

    private static final Class<Countdown> MODEL_CLASS = Countdown.class;
    private static final int MODEL_LAYOUT = R.layout.viewholder_countdown_list_item;
    private static final Class<ViewHolder> VIEWHOLDER_CLASS = ViewHolder.class;
    private static final DatabaseReference DATA_REFERENCE =
            OldDatabase.getCountdownsDataReference();

    private CountdownSelectionListener mSelectionListener;

    private List<ViewHolder> mSelectedViewHolders;

    private int mSortOption = DATE_CREATED;


    /**
     *  @param listener
     * @param keyRef
     */
    public CountdownRecyclerAdapter(CountdownSelectionListener listener, Query keyRef) {
        this(listener, keyRef, DATA_REFERENCE);
    }

    public CountdownRecyclerAdapter(CountdownSelectionListener listener, Query keyRef,
            Query dataRef) {
        super(MODEL_CLASS, MODEL_LAYOUT, VIEWHOLDER_CLASS, keyRef, (DatabaseReference) dataRef);
        mSelectionListener = listener;
        mSelectedViewHolders = new ArrayList<>();
    }

    private CountdownRecyclerAdapter(CountdownSelectionListener listener, Query keyRef,
            @SortOption int sortOption) {
        this(listener, keyRef, DATA_REFERENCE, sortOption);

    }

    private CountdownRecyclerAdapter(CountdownSelectionListener listener, Query keyRef,
            Query dataRef, @SortOption int sortOption) {
        super(MODEL_CLASS, MODEL_LAYOUT, VIEWHOLDER_CLASS, keyRef, (DatabaseReference) dataRef);
        mSelectionListener = listener;
        mSelectedViewHolders = new ArrayList<>();
        mSortOption = sortOption;
    }

    /**
     * Sorts the given list using one of the {@linkplain SortOptions} available
     *
     * @param list The list of countdowns
     * @param listener A callback to pass to a new CountdownRecyclerAdapter
     * @param option The startExtraSettingsIntent to sort the list by
     * @see SortOptions
     */
    public static void sortList(RecyclerView list, @NonNull CountdownSelectionListener listener,
            Query keyRef,
            @SortOption int option) {
        Query dataRef;
        switch (option) {
            default:
            case DATE_CREATED:
                // TODO: 5/27/17 Review option of sorting by "startTime" key
                dataRef = DATA_REFERENCE.orderByKey();
                break;
            case TIME_LEFT:
                dataRef = DATA_REFERENCE.orderByChild(OldDatabase.PATH_FINISH_TIME);
                break;
            case COUNTDOWN_LENGTH:
                dataRef = DATA_REFERENCE.orderByPriority();
                break;
        }
        list.setAdapter(new CountdownRecyclerAdapter(listener, keyRef, dataRef, option));
    }

    public static void filterList(RecyclerView list, CountdownSelectionListener listener,
            Query keyRef) {
        list.setAdapter(new CountdownRecyclerAdapter(listener, keyRef));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        mSelectionListener.onLoad();
    }

    @Override
    protected void populateViewHolder(final ViewHolder viewHolder, Countdown countdown,
            final int position) {
        viewHolder.setOnClickListener(
                v -> mSelectionListener.onCountdownSelected(getRef(position).getKey()));
//        viewHolder.setOnLongClickListener(v -> {
        // TODO: 5/31/17 Re-enable when appropriate
        // TODO: 5/31/17 Refactor into something manageable
//                mSelectionListener.onCountdownLongSelected(getRef(position).getKey());
//                viewHolder.setIsSelected(!viewHolder.getIsActivated());
//            return true;
//        });
        viewHolder.updateCountdown(countdown.getStartTime(), countdown.getFinishTime());
        viewHolder.updateCountdownSupportingText(countdown.getTitle(), countdown.getDescription());
    }

    @Nullable
    private CountdownSelectionListener getListener() {
        return mSelectionListener;
    }

    public List<ViewHolder> getSelectedViewHolders() {
        return mSelectedViewHolders;
    }

    public int getSortOption() {
        return mSortOption;
    }

    public void setSortOption(int option) {
        mSortOption = option;
    }

    public interface CountdownSelectionListener {

        void onLoad();

        /**
         * Called when a {@link Countdown} in a CountdownRecyclerAdapter is tapped
         *
         * @param countdownId The Firebase OldDatabase UID for the selected Countdown
         */
        void onCountdownSelected(String countdownId);

        /**
         * Called when a {@link Countdown} in a CountdownRecyclerAdapter is long-tapped
         *
         * @param countdownId The Firebase OldDatabase UID for the selected Countdown
         */
        void onCountdownLongSelected(String countdownId);
    }

    @SuppressWarnings("WeakerAccess")
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View mRootView;
        private TextView mCounter;
        private TextView mCountdownTitle;
        private TextView mCountdownInfo;
        private ImageView mCountCheckOverlay;
        private CircleImageView mCountBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            mCounter = itemView.findViewById(R.id.countdown_counter);
            mCountdownTitle = itemView.findViewById(R.id.countdown_title);
            mCountdownInfo = itemView.findViewById(R.id.countdown_info);
            mCountCheckOverlay = itemView.findViewById(R.id.countdown_counter_check);
            mCountBackground = itemView.findViewById(R.id.countdown_counter_background);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mRootView.setOnClickListener(listener);
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            mRootView.setOnLongClickListener(listener);
        }

        public void updateCountdownSupportingText(String nameText, String infoText) {
            mCountdownTitle.setText(nameText);
            mCountdownInfo.setText(infoText);
        }

        public void updateCountdown(long startTime, long endTime) {
            // TODO: Add settings for urgency trigger including user preference
            String unitType = Preferences.getInstance(mRootView.getContext())
                    .getDefaultUnitType(mCounter.getContext());
//            String unitsUntil = UnitsFormatter.creator(mCounter.getContext())
//                    .startingNow()
//                    .endingAt(endTime)
//                    .usingType(unitType)
//                    .asNumberLabel();
            // TODO: 6/23/17 Fix me
            int daysUntil = UnitsFormatter.getUnitsUntil(endTime, UnitsFormatter.DAYS);
            // Ensure no negative numbers aren't displayed
            if (daysUntil <= 0) {
                daysUntil = 0;
            }
            String countText = String.valueOf(daysUntil);
//            String countText = mRootView.getContext().getResources()
//                    .getQuantityString(R.plurals.countdown_unit_days, daysUntil);
            mCounter.setText(countText);

            // TODO: 3/15/17 Refactor this crap
//            UnitsFormatter.colorCountdownBackground(mCountBackground,
//                    UnitsFormatter.getUrgencyLevel(startTime,
//                            endTime, countdownUnitsOption));
        }

        public void updateUrgency(long startTime, long endTime) {

        }

        /**
         * Updates the UI to indicate this is selected
         *
         * @param isSelected The state the ViewHolder should be in
         */
        public void setIsSelected(boolean isSelected) {
            mCounter.setActivated(isSelected);

            int visibility = isSelected ? View.VISIBLE : View.GONE;
            mCountCheckOverlay.setVisibility(visibility);
            int backgroundColorStart = isSelected
                    ? R.color.countdown_background_not_selected
                    : R.color.countdown_background_selected;
            int backgroundColorEnd = !isSelected
                    ? R.color.countdown_background_not_selected
                    : R.color.countdown_background_selected;
            ValueAnimator valueAnimator = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                valueAnimator = ValueAnimator.ofArgb(
                        mRootView.getContext().getResources().getColor(backgroundColorStart),
                        mRootView.getContext().getResources().getColor(backgroundColorEnd));
            }
            valueAnimator.addUpdateListener(animation -> {
                mRootView.setBackgroundColor((Integer) animation.getAnimatedValue());
            });
            valueAnimator.start();

            int counterVisibility = !isSelected ? View.VISIBLE : View.GONE;
            mCounter.setVisibility(counterVisibility);
        }

        public boolean getIsActivated() {
            return mCounter.isActivated();
        }

    }
}
