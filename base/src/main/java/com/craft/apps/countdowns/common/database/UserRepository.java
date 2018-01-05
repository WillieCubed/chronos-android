package com.craft.apps.countdowns.common.database;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static com.craft.apps.countdowns.common.database.QuerySource.getUserRef;

/**
 * A source of data for {@link User} objects.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class UserRepository {

    /**
     * Returns a {@link android.arch.lifecycle.LiveData} that is notified on {@link User}
     * data changes.
     *
     * @param uid The UID of the {@link User} to monitor data changes
     */
    @NonNull
    public static MutableLiveData<User> getUser(String uid) {
        MutableLiveData<User> data = new MutableLiveData<>();
        User user = getUserRef(uid).get()
                .continueWith(new UserDocumentDeserializer()).getResult();
        data.setValue(user);
        return data;
    }

    /**
     * Returns the current information for the given {@link User} ID only once.
     */
    public static User fetchUser(String uid) {
        return getUserRef(uid).get()
                .continueWith(new UserDocumentDeserializer()).getResult();
    }

    /**
     * Replaces a {@link User}'s data with another.
     *
     * @param uid The target UID of the user to replace
     */
    @NonNull
    public static Task<Void> updateUser(String uid, User user) {
        return getUserRef(uid).set(user);
    }

    /**
     * Removes all the given user's data from the database.
     *
     * @param user The user to clear data
     * @see #clearUserData(String)
     */
    @NonNull
    public static Task<Void> clearUserData(User user) {
        return getUserRef(user.getUid()).delete();
    }

    /**
     * Removes all the given user's data from the database.
     *
     * @param uid The database UID of the user to clear data
     */
    @NonNull
    public static Task<Void> clearUserData(String uid) {
        return getUserRef(uid).set(new Object());
    }

    /**
     * Associates the given {@link Countdown} to the given {@link User}'s ID.
     */
    @NonNull
    public static Task<Void> linkCountdownToUser(String userId, Countdown countdown) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(countdown.getUid(), true);
        return getUserRef(userId).set(updates, SetOptions.merge());
    }

    /**
     * Adds the given FCM token to the given user's messaging tokens.
     */
    @NonNull
    public static Task<Void> addFcmToken(User user, String token) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(token, true);
        return getUserRef(user.getUid()).set(updates, SetOptions.merge());
    }
}
