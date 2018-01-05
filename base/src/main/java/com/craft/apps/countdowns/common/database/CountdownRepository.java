package com.craft.apps.countdowns.common.database;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.data.FirestoreQueryLiveData;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;
import com.google.android.gms.tasks.Task;

import static com.craft.apps.countdowns.common.database.QuerySource.COUNTDOWNS;
import static com.craft.apps.countdowns.common.database.QuerySource.getCountdownRef;

/**
 * A database handler that persists, modifies, and fetches {@link Countdown}s to and from the
 * remote database.
 *
 * @version 2.0.0
 * @since 1.0.0
 */
public class CountdownRepository {

    private static final String TAG = CountdownRepository.class.getSimpleName();

    private CountdownRepository() {
    }

    /**
     * Fetches a {@link Countdown} in an easily monitor-able way.
     *
     * @param id The UID of the {@link Countdown} to fetch
     * @return A {@link MutableLiveData} that will update when countdown data changes.
     */
    @NonNull
    public static MutableLiveData<Countdown> getCountdown(String id) {
        return new FirestoreQueryLiveData<>(
                QuerySource.COUNTDOWNS.whereEqualTo("uid", id), Countdown.class);
    }

    // TODO: 12/30/2017 Move to AppDatabase

    /**
     * Persists the given {@link Countdown} to the database and associates it with the given
     * user's UID.
     *
     * @param countdown A {@link Countdown} that will have its UID updated when the operation is
     * successful
     */
    @NonNull
    public static Task<Countdown> uploadCountdown(Countdown countdown) {
        String docId = COUNTDOWNS.document().getId();
        return COUNTDOWNS.document().set(countdown)
                .continueWith(task -> {
                    countdown.setUid(docId);
                    return countdown;
                });
    }

    /**
     * Replaces the countdown with the given ID with the given one.
     *
     * @param id The location to put the new countdown
     * @param countdown The countdown to replace the existing one
     */
    @NonNull
    public static Task<Void> updateCountdown(String id, Countdown countdown) {
        return getCountdownRef(id).set(countdown);
    }

    /**
     * Removes the given countdown the database.
     *
     * @param countdownId The countdown to delete
     */
    @NonNull
    public static Task<Void> deleteCountdown(String countdownId) {
        // TODO: 12/31/2017 Update backend to remove it from user records
        return getCountdownRef(countdownId).delete();
    }

    /**
     * Removes the {@link Countdown} with the given record from the database.
     * TODO: 12/31/2017 Move to AppDatabase
     *
     * @see #deleteCountdown(String)
     */
    @NonNull
    public static Task<Void> deleteCountdownFromUser(String countdownId, String userId) {
        User user = UserRepository.fetchUser(userId);
        user.getCountdowns().remove(countdownId);
        return UserRepository.updateUser(userId, user);
    }

//    /**
//     * Monitors the given user's {@link Countdown}s for changes.
//     * TODO: 12/30/2017 Move to AppDatabase
//     *
//     * @param userId
//     * @param observer An observer that is called when a {@link Countdown} is modified
//     */
//    public void monitorCountdowns(String userId, Observer<Countdown> observer) {
//        getUsersCollection().addSnapshotListener((snapshots, e) -> {
//            if (e != null) {
//                Log.w(TAG, "onEvent: Listen failed", e);
//                return;
//            }
//            for (DocumentSnapshot snapshot : snapshots.getDocuments()) {
//                Countdown countdown = snapshot.toObject(Countdown.class);
//                observer.onChanged(countdown);
//            }
//        });
//    }

    /**
     * Returns a {@link Task<Countdown>} containing the countdown with the given UID.
     *
     * @param id The UID of the {@link Countdown} to fetch
     */
    @NonNull
    public Task<Countdown> fetchCountdown(String id) {
        return getCountdownRef(id).get()
                .continueWith(new CountdownDocumentDeserializer());
    }
}
