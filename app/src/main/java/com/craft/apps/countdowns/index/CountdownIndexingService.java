package com.craft.apps.countdowns.index;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.craft.apps.countdowns.auth.UserManager;
import com.craft.apps.countdowns.common.data.CountdownListDeserializer;
import com.craft.apps.countdowns.common.database.QuerySource;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;

/**
 *
 * TODO: Update to new Firebase Indexing BroadcastService
 * @version 1.0.0
 * @since 1.0.0
 */
public class CountdownIndexingService extends IntentService {

    private static final String TAG = CountdownIndexingService.class.getSimpleName();

    /**
     * Creates an IntentService
     */
    public CountdownIndexingService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        User user = UserManager.getCurrentUser();
        if (user != null) {
            QuerySource.countdownsForUser(user).get()
                    .continueWith(new CountdownListDeserializer())
                    .addOnSuccessListener(countdowns -> {
                        for (Countdown countdown : countdowns) {
                            Indexer.indexCountdown(countdown, user);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error while indexing countdowns", e);
                    });
        }
    }

    private String generateCountdownLabel(Countdown countdown) {
        // TODO: 3/15/17 Refactor this into proper place
//        DateTime dateTime = countdown.toDateTime();
//        String month = String.valueOf(dateTime.getMonthOfYear());
//        String dayOfMonth = String.valueOf(dateTime.getDayOfMonth());
//        String year = String.valueOf(dateTime.getYear());
//        String dateString = month + "-" + dayOfMonth + "-" + year;
//        return "Countdown to " + countdown.getTitle() + " on " + dateString;
        return countdown.getTitle();
    }
}
