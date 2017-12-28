package com.craft.apps.countdowns.notification;

import android.util.Log;

import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.util.Users;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public class CountdownInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = CountdownInstanceIdService.class.getSimpleName();

    /**
     * Persists an updated instance ID token to the signed in user's Firebase OldDatabase reference.
     *
     * @param token A new token to persist
     */
    private static void sendRegistrationToServer(String token) {
        FirebaseUser user = Users.getCurentUser();
        if (user != null) {
            OldDatabase.getUserReference(user.getUid()).child(OldDatabase.PATH_FCM_TOKENS)
                    .child(token).setValue(true, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    Log.e(TAG, "onComplete: Couldn't update user FCM token; " +
                            databaseError.getDetails(), databaseError.toException());
                } else {
                    Log.i(TAG, "onComplete: Successfully updated user FCM token.");
                }
            });
        }
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh: Attempting to persist new token " + refreshedToken
                + " to database");
        sendRegistrationToServer(refreshedToken);
    }
}
