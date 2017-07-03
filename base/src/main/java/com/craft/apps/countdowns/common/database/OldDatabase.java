package com.craft.apps.countdowns.common.database;

import android.util.Log;
import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

// TODO: 6/29/17 Refactor into Database interface

/**
 * A utility class that abstracts the Firebase OldDatabase backend to semantically sound
 * helper methods
 *
 * @author Willie Chalmers III
 * @version 1.0.0
 * @since v1.0.0 (3/17/17)
 */
@SuppressWarnings("WeakerAccess")
public class OldDatabase {

    /**
     * The database path for all the service's users
     */
    public static final String PATH_USERS = "users";
    /**
     * The database path for all the service's {@link Countdown}s
     */
    public static final String PATH_COUNTDOWNS = "countdowns";
    /**
     * The attribute for a {@link Countdown}'s start time
     */
    public static final String PATH_START_TIME = "startTime";
    /**
     * The attribute for a {@link Countdown}'s end time
     */
    public static final String PATH_FINISH_TIME = "finishTime";
    /**
     * The database path for a user's FCM tokens
     */
    public static final String PATH_FCM_TOKENS = "fcmTokens";
    private static final String TAG = OldDatabase.class.getSimpleName();
    private DatabaseReference mDatabaseReference;

    OldDatabase() {

    }

    /**
     * @return The database location of the user's countdown
     */
    public static DatabaseReference getUserReference(String userId) {
        return FirebaseDatabase.getInstance().getReference().child(PATH_USERS).child(userId);
    }

    public static DatabaseReference getUserCountdownsReference(String userId) {
        return getUserReference(userId).child(PATH_COUNTDOWNS);
    }

    public static DatabaseReference getCountdownsDataReference() {
        return FirebaseDatabase.getInstance().getReference().child(PATH_COUNTDOWNS);
    }

    public static DatabaseReference getCountdownReference(String countdownId) {
        return FirebaseDatabase.getInstance().getReference().child(PATH_COUNTDOWNS)
                .child(countdownId);
    }

    public static DatabaseReference createCountdownReference() {
        return getCountdownsDataReference().push();
    }

    public static void deleteUserCountdown(String countdownId, String userId) {
        getUserCountdownsReference(userId).child(countdownId)
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        Log.i(TAG, "onComplete: Successfully deleted user countdown reference");
                    } else {
                        Log.w(TAG, "onComplete: Error when deleting user countdown reference",
                                databaseError.toException());
                    }
                });
        getCountdownReference(countdownId).removeValue((databaseError, databaseReference) -> {
            if (databaseError == null) {
                Log.i(TAG, "onComplete: Successfully deleted countdown "
                        + databaseReference.getKey());
            } else {
                Log.w(TAG, "onComplete: Error when deleting countdown "
                        + databaseReference.getKey(), databaseError.toException());
            }
        });
    }

    public static void deleteUserCountdown(String countdownId, String userId,
            CompletionListener listener) {
        getUserCountdownsReference(userId).child(countdownId).removeValue(listener);
        getCountdownReference(countdownId).removeValue(listener);
    }
}
