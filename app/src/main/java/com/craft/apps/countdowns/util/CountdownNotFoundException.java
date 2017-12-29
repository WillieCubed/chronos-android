package com.craft.apps.countdowns.util;

import com.craft.apps.countdowns.common.model.Countdown;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public class CountdownNotFoundException extends IllegalArgumentException {

    /**
     * Creates an exception with an custom error message
     *
     * @param countdownId The invalid {@link Countdown} in question
     */
    public CountdownNotFoundException(String countdownId) {
        super("Countdown not found for ID " + countdownId);
    }
}
