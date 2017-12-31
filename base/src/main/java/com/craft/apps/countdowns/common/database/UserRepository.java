package com.craft.apps.countdowns.common.database;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * A source of data for {@link User} objects.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class UserRepository {

    private static final String KEY_USERS = "users";

    /**
     * Returns a {@link android.arch.lifecycle.LiveData} that is notified on {@link User}
     * data changes.
     *
     * @param uid The UID of the {@link User} to monitor data changes
     */
    @NonNull
    public static MutableLiveData<User> getUser(String uid) {
        MutableLiveData<User> data = new MutableLiveData<>();
        User user = getUsersCollection().document(uid).get()
                .continueWith(new UserDocumentDeserializer()).getResult();
        data.setValue(user);
        return data;
    }

    /**
     * Returns the current information for the given {@link User} ID only once.
     */
    @NonNull
    public static User fetchUser(String uid) {
        return getUsersCollection().document(uid).get()
                .continueWith(new UserDocumentDeserializer()).getResult();
    }

    /**
     * Replaces a {@link User}'s data with another.
     *
     * @param uid The target UID of the user to replace
     */
    @NonNull
    public static Task<Void> updateUser(String uid, User user) {
        return getUsersCollection().document(uid).set(user);
    }

    /**
     * Removes all the given user's data from the database.
     *
     * @param user The user to clear data
     * @see #clearUserData(String)
     */
    @NonNull
    public static Task<Void> clearUserData(User user) {
        return getUsersCollection().document(user.getUid()).set(new Object());
    }

    /**
     * Removes all the given user's data from the database.
     *
     * @param uid The database UID of the user to clear data
     */
    @NonNull
    public static Task<Void> clearUserData(String uid) {
        return getUsersCollection().document(uid).set(new Object());
    }

    /**
     * Associates the given {@link Countdown} to the given {@link User}'s ID.
     */
    @NonNull
    public static Task<Void> linkCountdownToUser(String userId, Countdown countdown) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(countdown.getUid(), true);
        return getUsersCollection().document(userId).set(updates, SetOptions.merge());
    }

    @NonNull
    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(KEY_USERS);
    }
}
