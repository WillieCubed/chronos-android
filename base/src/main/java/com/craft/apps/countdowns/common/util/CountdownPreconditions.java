package com.craft.apps.countdowns.common.util;

import android.os.Bundle;

/**
 * A class of utility methods for checking the validity of
 * {@link com.craft.apps.countdowns.common.model.Countdown}-related arguments
 *
 * @author willie
 * @version 1.0.0
 * @since 6/24/17
 */
public final class CountdownPreconditions {

    /**
     * @param args Either {@link android.app.Activity#getIntent()} arguments or {@link
     * android.support.v4.app.Fragment} arguments
     * @return The {@link IntentUtils#ARG_COUNTDOWN_ID}
     *
     * @throws IllegalArgumentException When the given arguments don't
     * @throws IllegalStateException When
     */
    public static String checkValidArgs(Bundle args) {
        Preconditions.checkNotNull(args);
        if (!args.containsKey(IntentUtils.ARG_COUNTDOWN_ID)) {
            throw new IllegalStateException(
                    "Argument must contain key " + IntentUtils.ARG_COUNTDOWN_ID);
        }
        Preconditions.checkNotNull(args.getString(IntentUtils.ARG_COUNTDOWN_ID));
        return args.getString(IntentUtils.ARG_COUNTDOWN_ID);
    }

    /**
     * Provides a silently failing way to fetch {@link IntentUtils#ARG_COUNTDOWN_ID}
     *
     * @param args Either {@link android.app.Activity#getIntent()} arguments or {@link
     * android.support.v4.app.Fragment} arguments
     * @return The {@link IntentUtils#ARG_COUNTDOWN_ID} if the arguments contain it, null otherwise
     */
    public static String ifAvailable(Bundle args) {
        if (args == null) {
            // There's probably an even more compact way to do this
            return null;
        }
        return args.containsKey(IntentUtils.ARG_COUNTDOWN_ID)
                ? args.getString(IntentUtils.ARG_COUNTDOWN_ID)
                : null;
    }
}
