package com.craft.apps.countdowns.notification;

import android.util.Log;

import com.craft.apps.countdowns.auth.UserManager;
import com.craft.apps.countdowns.common.database.UserRepository;
import com.craft.apps.countdowns.common.model.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * @version 1.0.1
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
        User user = UserManager.getCurrentUser();
        if (user != null) {
            UserRepository.addFcmToken(user, token)
                    .addOnSuccessListener(success -> {
                        Log.i(TAG, "Successfully updated user FCM token");
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Couldn't update user FCM Token", e);
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
