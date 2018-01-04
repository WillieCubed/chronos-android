package com.craft.apps.countdowns.common.database;

import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Set;

/**
 * A set of common queries for database queries that can't be fulfilled with {@link
 * CountdownRepository} and {@link UserRepository}.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class QuerySource {

    private static final String KEY_USERS = "users";
    /**
     * The collection of all the app's {@link User}s.
     */
    public static final CollectionReference USERS = getFirestore().collection(KEY_USERS);
    private static final String KEY_PRIVILEGES = "privileges";
    private static final String KEY_COUNTDOWNS = "countdowns";
    /**
     * The collection of all the app's {@link Countdown}s.
     */
    public static final CollectionReference COUNTDOWNS = getFirestore().collection(KEY_COUNTDOWNS);

    /**
     * Returns the {@link DocumentReference} for the given countdown ID.
     *
     * @return The data source for a {@link Countdown}
     */
    @NonNull
    public static DocumentReference getCountdownRef(String uid) {
        return COUNTDOWNS.document(uid);
    }

    /**
     * Returns the {@link DocumentReference} for the given user ID.
     *
     * @return The data source for a {@link User}
     */
    @NonNull
    public static DocumentReference getUserRef(String userId) {
        return USERS.document(userId);
    }

    /**
     * Returns the given number of {@link Countdown}s created most recently.
     *
     * @param n The number of Countdowns to fetch
     */
    @NonNull
    public static Query lastNCountdowns(int n) {
        return COUNTDOWNS.limit(n);
    }

    /**
     * Returns a {@link Query} representing all the {@link Countdown}s for the given {@link User}.
     *
     * @param user The User with the Countdowns to fetch
     */
    @NonNull
    public static Query countdownsForUser(User user) {
        Set<String> countdowns = user.getCountdowns().keySet();
        String[] ids = countdowns.toArray(new String[countdowns.size()]);
        return COUNTDOWNS.whereEqualTo("uid", FieldPath.of(ids));
    }

    @NonNull
    private static FirebaseFirestore getFirestore() {
        return FirebaseFirestore.getInstance();
    }
}
