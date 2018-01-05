package com.craft.apps.countdowns.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.firebase.ui.database.FirebaseIndexListAdapter;
import com.google.firebase.database.Query;

/**
 * A {@link android.widget.ListAdapter} that displays {@link Countdown}s
 *
 * @author willie
 * @version 1.0.0
 * @since 4/18/17
 */
public class CountdownWidgetListAdapter extends FirebaseIndexListAdapter<Countdown> {

    public CountdownWidgetListAdapter(Activity context, Query keyRef) {
        super(context, Countdown.class, R.layout.viewholder_countdown_list_item, keyRef,
                OldDatabase.getCountdownsDataReference());
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(mLayout, viewGroup, false);
            view.setTag(new ViewHolder(view));
        }

        populateView(view, getItem(position), position);
        return view;
    }

    @Override
    protected void populateView(View view, Countdown model, int position) {
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
