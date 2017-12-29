package com.craft.apps.countdowns.common.data;

import android.arch.core.util.Function;
import android.util.Log;

import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A function that maps a {@link DataSnapshot} into a list of {@link Countdown}s.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class CountdownListDeserializer implements Function<DataSnapshot, List<Countdown>> {

    private static final String TAG = CountdownListDeserializer.class.getSimpleName();

    @Override
    public List<Countdown> apply(DataSnapshot input) {
        List<Countdown> countdowns = new ArrayList<>();
        try {
            for (DataSnapshot dataSnapshot : input.getChildren()) {
                countdowns.add(dataSnapshot.getValue(Countdown.class));
            }
        } catch (Exception e) {
            // Recoverable
            Log.w(TAG, "Error when fetching countdowns", e);
        }
        return countdowns;
    }
}
