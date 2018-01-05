package com.craft.apps.countdowns.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.database.UserRepository;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;
import com.craft.apps.countdowns.common.util.Preconditions;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link RemoteViewsService} for handling asynchronous {@link CountdownListWidget} data
 * requests.
 *
 * @version 2.0.0
 * @since 1.0.0
 */
public class CountdownListWidgetService extends RemoteViewsService {

    /**
     * A parameter for a user's UID.
     */
    public static final String EXTRA_USER_ID = "com.craft.apps.countdowns.extra.USER_ID";

    private static final String TAG = CountdownListWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Preconditions.checkNotNull(intent.getStringExtra(EXTRA_USER_ID));
        return new CountdownListRemoteViewsFactory(getApplicationContext(),
                intent.getStringExtra(EXTRA_USER_ID));
    }

    private static class CountdownListRemoteViewsFactory extends
            FirestoreListRemoteViewsFactory<Countdown> {

        /**
         * Creates a new CountdownListRemoteViewsFactory.
         *
         * @param userId The user for which to fetch {@link Countdown} data
         */
        public CountdownListRemoteViewsFactory(Context context, String userId) {
            super(getQuery(userId), context, Countdown.class);
        }

        // TODO: 12/31/2017 Decouple this
        private static Query getQuery(String userId) {
            User user = UserRepository.fetchUser(userId);
            List<String> list = new ArrayList<>();
            list.addAll(user.getCountdowns().keySet());
            String[] userCountdowns = list.toArray(new String[list.size()]);
            return FirebaseFirestore.getInstance().collection("countdowns")
                    .whereEqualTo("uid", FieldPath.of(userCountdowns));
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(getContext().getPackageName(),
                    R.layout.viewholder_countdown_list_item);
            long finishTime = getItem(position).getFinishTime();
            // TODO: 6/28/17 Allow specification of unit type
            int units = UnitsFormatter.getUnitsUntil(finishTime, UnitsFormatter.DAYS);
            String pluralText = getContext().getResources()
                    .getQuantityString(R.plurals.countdown_unit_days, units, units);
            remoteViews.setTextViewText(R.id.countdown_counter, pluralText);
            remoteViews.setTextViewText(R.id.countdown_title, getItem(position).getTitle());
            return remoteViews;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }
    }
}
