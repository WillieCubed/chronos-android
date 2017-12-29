package com.craft.apps.countdowns.common.privilege;

import android.support.annotation.StringDef;
import android.util.Log;

import com.craft.apps.countdowns.common.BuildConfig;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An abstract method of accessing states of service-level feature privileges for users.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
// TODO: 7/2/17 Ensure absolute consistency
public class UserPrivileges {

    /**
     * A constant denoting whether the service should <em>not</em> present ads to the user
     */
    public static final String DISABLED_ADS = "disableAds";

    private static final String TAG = UserPrivileges.class.getSimpleName();
    private static final String PRIVILEGES_KEY = "privileges";

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
        // Real magic happens here
        OldDatabase.getUserReference(userId).child(PRIVILEGES_KEY).child(privilege)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean hasPrivilege = dataSnapshot.getValue(Boolean.class);
                        Log.d(TAG, "onDataChange: User privilege " + privilege + " is "
                                + hasPrivilege);
                        callback.onResult(hasPrivilege != null ? hasPrivilege : false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "onCancelled: Error when fetching user privilege " + privilege,
                                databaseError.toException());
                        callback.onResult(false);
                    }
                });
    }

    /**
     * @param privilege A privilege denoted by {@link Privilege}
     * @param userId The ID of a user that exists in the database
     */
    public static void enableFor(@Privilege String privilege, String userId,
                                 PrivilegeCallback callback) {
        OldDatabase.getUserReference(userId).child(PRIVILEGES_KEY).child(privilege)
                .setValue(true).addOnSuccessListener(aVoid -> {
            Log.i(TAG, "enableFor: Privilege successfully set for " + userId);
            callback.onResult(true);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "enableFor: Error when setting privilege for " + userId, e);
        });
    }

    /**
     * @param privilege A privilege denoted by {@link Privilege}
     * @param userId The ID of a user that exists in the database
     */
    public static void disableFor(@Privilege String privilege, String userId,
                                  PrivilegeCallback callback) {
        OldDatabase.getUserReference(userId).child(PRIVILEGES_KEY).child(privilege)
                .setValue(false).addOnSuccessListener(aVoid -> {
            Log.i(TAG, "disableFor: Privilege successfully set for " + userId);
            callback.onResult(false);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "disableFor: Error when setting privilege for " + userId, e);
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
