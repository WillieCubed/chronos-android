package com.craft.apps.countdowns.index;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.util.Users;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * @author willie
 * @version 1.0.0
 * @since 3/14/17
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
        final FirebaseUser user = Users.getCurentUser();
        if (user != null) {
            String userId = user.getUid();
            OldDatabase.getUserCountdownsReference(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d(TAG, "onDataChange: Indexing countdown " + snapshot.getKey());
                                Indexer.indexCountdown(snapshot.getValue(Countdown.class),
                                        snapshot.getKey(), user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "Indexing error while getting user countdown keys",
                                    databaseError.toException());
                        }
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
