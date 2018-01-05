package com.craft.apps.countdowns.common.data;

import android.arch.core.util.Function;

import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.database.DataSnapshot;

/**
 * A function that maps {@link DataSnapshot}s to {@link Countdown}s.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class SingleCountdownDeserializer implements Function<DataSnapshot, Countdown> {

    @Override
    public Countdown apply(DataSnapshot input) {
        return input.getValue(Countdown.class);
    }
}
