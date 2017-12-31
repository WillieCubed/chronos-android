package com.craft.apps.countdowns.common.database;

import android.util.Log;

import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A utility class that abstracts the Firebase OldDatabase backend to semantically sound
 * helper methods.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @deprecated Use {@link CountdownRepository} and {@link UserRepository}
 */
@SuppressWarnings("WeakerAccess")
@Deprecated
public class OldDatabase {

    /**
     * The database path for all the service's users
     */
    @Deprecated
    public static final String PATH_USERS = "users";
    /**
     * The database path for all the service's {@link Countdown}s
     */
    @Deprecated
    public static final String PATH_COUNTDOWNS = "countdowns";
    /**
     * The attribute for a {@link Countdown}'s start time
     */
    @Deprecated
    public static final String PATH_START_TIME = "startTime";
    /**
     * The attribute for a {@link Countdown}'s end time
     */
    @Deprecated
    public static final String PATH_FINISH_TIME = "finishTime";
    /**
     * The database path for a user's FCM tokens
     */
    @Deprecated
    public static final String PATH_FCM_TOKENS = "fcmTokens";
    private static final String TAG = OldDatabase.class.getSimpleName();
    private DatabaseReference mDatabaseReference;

    @Deprecated
    OldDatabase() {

    }

    /**
     * @return The database location of the user's countdown
     */
    @Deprecated
    public static DatabaseReference getUserReference(String userId) {
        return FirebaseDatabase.getInstance().getReference().child(PATH_USERS).child(userId);
    }

    @Deprecated
    public static DatabaseReference getUserCountdownsReference(String userId) {
        return getUserReference(userId).child(PATH_COUNTDOWNS);
    }

    @Deprecated
    public static DatabaseReference getCountdownsDataReference() {
        return FirebaseDatabase.getInstance().getReference().child(PATH_COUNTDOWNS);
    }

    @Deprecated
    public static DatabaseReference getCountdownReference(String countdownId) {
        return FirebaseDatabase.getInstance().getReference().child(PATH_COUNTDOWNS)
                .child(countdownId);
    }

    @Deprecated
    public static DatabaseReference createCountdownReference() {
        return getCountdownsDataReference().push();
    }

    @Deprecated
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

    @Deprecated
    public static void deleteUserCountdown(String countdownId, String userId,
                                           CompletionListener listener) {
        getUserCountdownsReference(userId).child(countdownId).removeValue(listener);
        getCountdownReference(countdownId).removeValue(listener);
    }
}
