package com.craft.apps.countdowns.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.Query;

/**
 * A {@link android.widget.ListAdapter} that displays {@link Countdown}s
 *
 * @author willie
 * @version 1.0.0
 * @since 4/18/17
 */
public class CountdownWidgetListAdapter extends FirebaseListAdapter<Countdown> {

    private final Context mContext;

    public CountdownWidgetListAdapter(Activity context, Query keyRef) {
        super(new FirebaseListOptions.Builder<Countdown>()
                .setQuery(keyRef, Countdown.class).build());
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(mLayout, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }

        populateView(view, getItem(position), position);
        return view;
    }

    @Override
    protected void populateView(View view, @NonNull Countdown model, int position) {
        ViewHolder viewHolder;
        if (view.getTag() != null) {
            viewHolder = new ViewHolder(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.setCount(
                UnitsFormatter.getUnitsUntil(model.getFinishTime(), UnitsFormatter.DAYS));
        viewHolder.setTitle(model.getTitle());
    }

    /**
     * A class that helps avoid costly {@link View#findViewById(int)} lookups
     */
    static class ViewHolder {

        TextView mCountView;

        TextView mTitleView;

        /**
         * Constructs a view holder
         */
        public ViewHolder(View itemView) {
            mCountView = itemView.findViewById(R.id.countdown_counter);
            mTitleView = itemView.findViewById(R.id.countdown_title);
        }

        public void setCount(int count) {
            // TODO: 6/27/17 Fetch default unit
            String text = mCountView.getContext().getResources()
                    .getQuantityString(R.plurals.countdown_unit_days, count, count);
            mCountView.setText(text);
        }

        public void setTitle(String title) {
            mTitleView.setText(title);
        }
    }
}
