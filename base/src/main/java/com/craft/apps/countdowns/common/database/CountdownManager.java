package com.craft.apps.countdowns.common.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.craft.apps.countdowns.common.R;
import com.craft.apps.countdowns.common.analytics.CountdownAnalytics;
import com.craft.apps.countdowns.common.model.Countdown;
import com.google.android.gms.tasks.Task;

/**
 * @version 1.0.0
 * @since 1.0.0
 * @deprecated Use {@link CountdownRepository} instead
 */
@Deprecated
public class CountdownManager extends OldDatabase {

    private static final String TAG = CountdownManager.class.getSimpleName();

    @Deprecated
    public static Task<Void> uploadCountdown(@NonNull Countdown countdown, String countdownId) {
        return getCountdownReference(countdownId).setValue(countdown);
    }

    @Deprecated
    public static Task<Void> addCountdownToUser(String countdownId, String uid) {
        return getUserCountdownsReference(uid).child(countdownId).setValue(true);
    }

    @Deprecated
    public static Task<Void> uploadCountdownInUserAndDataRef() {
        return null;
    }

    @Deprecated
    public static String getNewCountdownId() {
        return getCountdownsDataReference().push().getKey();
    }

    /**
     * Persists the given countdown to the database
     *
     * @param uid A user ID from the database
     */
    @Deprecated
    public static void uploadCountdown(Context context, String uid, @NonNull Countdown countdown) {
        // Upload data to database then use data key to index for user
        String countdownId = getCountdownsDataReference().push().getKey();
        getCountdownReference(countdownId).setValue(countdown, (error, reference) -> {
            if (error == null) {
                uploadCountdownToUserRef(context, uid, countdownId);
            } else {
                Log.w(TAG, "uploadCountdown: Error when uploading countdown", error.toException());
            }
        });
    }

    /**
     * Removes the given countdown from the database
     *
     * @param uid A user ID from the database
     */
    @Deprecated
    public static void deleteCountdown(Context context, String uid, @NonNull String countdownId) {
        getCountdownReference(countdownId).removeValue((error, reference) -> {
            if (error == null) {
                deleteCountdownFromUserRef(context, uid, countdownId);

            } else {
                Log.w(TAG, "deleteCountdown: Error when deleting countdown", error.toException());
            }
        });
    }

    private static void uploadCountdownToUserRef(Context context, String uid, String key) {
        getUserCountdownsReference(uid).child(key).setValue(true, (error, reference) -> {
            if (error == null) {
                Log.i(TAG, "uploadCountdown: Countdown successfully uploaded to " + key);
                CountdownAnalytics.getInstance(context).logCreation(key);
            } else {
                Log.w(TAG, "uploadCountdown: Error when uploading countdown to " + key,
                        error.toException());
                Toast.makeText(context, R.string.error_uploading_countdown, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private static void deleteCountdownFromUserRef(Context context, String uid, String key) {
        getUserCountdownsReference(uid).child(key).removeValue((error, reference) -> {
            if (error == null) {
                Log.i(TAG, "deleteCountdownFromUserRef: Countdown successfully deleted from user "
                        + key);
                CountdownAnalytics.getInstance(context).logDeletion(key);
            } else {
                Log.w(TAG, "deleteCountdownFromUserRef: Error when deleting countdown from user "
                        + key, error.toException());
            }
        });
    }
}
