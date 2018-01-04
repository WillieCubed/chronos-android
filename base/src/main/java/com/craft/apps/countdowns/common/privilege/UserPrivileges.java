package com.craft.apps.countdowns.common.privilege;

import android.support.annotation.StringDef;
import android.util.Log;

import com.craft.apps.countdowns.common.BuildConfig;
import com.craft.apps.countdowns.common.database.QuerySource;
import com.craft.apps.countdowns.common.database.UserRepository;
import com.craft.apps.countdowns.common.model.User;
import com.google.firebase.firestore.SetOptions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract method of accessing states of service-level feature privileges for users.
 *
 * @version 1.1.0
 * @since 1.0.0
 */
public final class UserPrivileges {

    /**
     * A constant denoting whether the service should <em>not</em> present ads to the user
     */
    public static final String DISABLED_ADS = "disableAds";

    private static final String TAG = UserPrivileges.class.getSimpleName();

    /**
     * Fetches service privileges for the given privilege and returns the value in the given
     * callback
     *
     * @param privilege A privilege denoted by {@link Privilege}
     * @param userId The ID of a user that exists in the database
     */
    public static void fetchFor(@Privilege String privilege, PrivilegeCallback callback,
                                String userId) {
        // For testing/marketing, automatically grants any privilege if this is demo user
        if (BuildConfig.DEBUG) {
            callback.onResult(true);
            return;
        }
        Map<String, Object> privileges = UserRepository.fetchUser(userId).getPrivileges();
        if (privileges != null) {
            boolean hasPrivilege = privileges.containsKey(privilege);
            Log.d(TAG, "User privilege " + privilege + "is " + hasPrivilege);
            callback.onResult(hasPrivilege);
        } else {
            Log.w(TAG, "Error when fetching privileges");
            callback.onResult(false);
        }
    }

    /**
     * @param privilege A privilege denoted by {@link Privilege}
     * @param userId The ID of a user that exists in the database
     */
    public static void enableFor(@Privilege String privilege, String userId,
                                 PrivilegeCallback callback) {
        User user = UserRepository.fetchUser(userId);
        if (user.getPrivileges() != null) {
            user.getPrivileges().put(privilege, true);
            UserRepository.updateUser(userId, user)
                    .addOnSuccessListener(success -> {
                        Log.i(TAG, "Privilege set for " + userId);
                        callback.onResult(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error setting privilege for " + userId, e);
                        callback.onResult(false);
                    });
        }
    }

    /**
     * @param privilege A privilege denoted by {@link Privilege}
     * @param userId The ID of a user that exists in the database
     */
    public static void disableFor(@Privilege String privilege, String userId,
                                  PrivilegeCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(privilege, false);
        QuerySource.getUserRef(userId).set(updates, SetOptions.merge())
                .addOnSuccessListener(success -> {
                    Log.i(TAG, "Disabled privilege " + privilege + " for " + userId);
                    callback.onResult(false);
                })
                .addOnFailureListener(e -> {
                    Log.i(TAG, "Error disabling privilege " + privilege + " for " + userId);
                    callback.onResult(true);
                });
    }

    /**
     * An annotation which indicates the given parameter requires one of {@link #DISABLED_ADS}
     */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({DISABLED_ADS})
    public @interface Privilege {
        // no-op
    }

    /**
     * A callback called when a feature request returns
     */
    public interface PrivilegeCallback {

        /**
         * Is called when the backend returns the privilege state of a user
         *
         * @param hasPrivilege True if the privilege should be granted within the app
         */
        void onResult(boolean hasPrivilege);
    }

}
