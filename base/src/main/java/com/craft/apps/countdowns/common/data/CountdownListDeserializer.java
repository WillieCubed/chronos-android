package com.craft.apps.countdowns.common.data;

import android.arch.core.util.Function;
import android.util.Log;

import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A function that maps a {@link com.google.firebase.firestore.DocumentReference} into a list
 * of {@link Countdown}s.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class CountdownListDeserializer implements Function<QuerySnapshot, List<Countdown>> {

    private static final String TAG = CountdownListDeserializer.class.getSimpleName();

    @Override
    public List<Countdown> apply(QuerySnapshot input) {
        List<Countdown> countdowns = new ArrayList<>();
        try {
            countdowns = input.toObjects(Countdown.class);
        } catch (Exception e) {
            // Recoverable
            Log.w(TAG, "Error when fetching countdowns", e);
        }
        return countdowns;
    }
}
