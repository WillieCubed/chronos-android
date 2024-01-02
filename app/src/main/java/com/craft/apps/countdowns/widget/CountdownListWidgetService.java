package com.craft.apps.countdowns.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.util.Users;
import com.firebase.ui.database.ChangeEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (6/28/17)
 */
public class CountdownListWidgetService extends RemoteViewsService {

    public static final String EXTRA_USER_ID = "com.craft.apps.countdowns.extra.USER_ID";

    private static final String TAG = CountdownListWidgetService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CountdownListRemoteViewsFactory(getApplicationContext(), intent);
    }

    private static class CountdownListRemoteViewsFactory implements RemoteViewsFactory {

        private List<Countdown> countdownList = new ArrayList<>();

        private final Context context;

        public CountdownListRemoteViewsFactory(Context context, Intent intent) {
            super();
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return countdownList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.viewholder_countdown_list_item);
            long finishTime = getItem(position).getFinishTime();
            // TODO: 6/28/17 Allow specification of unit type
            int units = UnitsFormatter.getUnitsUntil(finishTime, UnitsFormatter.DAYS);
            String pluralText = context.getResources()
                    .getQuantityString(R.plurals.countdown_unit_days, units, units);
            remoteViews.setTextViewText(R.id.countdown_counter, pluralText);
            remoteViews.setTextViewText(R.id.countdown_title, getItem(position).getTitle());
            return remoteViews;
        }

        private Countdown getItem(int index) {
            return countdownList.get(index);
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
