package com.craft.apps.countdowns.common.model;

import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A set creator constants used to differentiate what state a list containing countdowns is in.
 *
 * @author willie
 * @version 1.0.0
 * @since 5/27/17
 */
public class SortOptions {

    /**
     * Sort by time creator creation
     * (Earlier dates -> later dates)
     */
    public static final int DATE_CREATED = 0;
    /**
     * Sort by time until finishTime - currentTime = 0
     * (Lesser delta time -> greater delta time
     */
    public static final int TIME_LEFT = 1;
    /**
     * Sort by increasing size creator finishTime - startTime
     * (Shortest countdowns -> largest countdowns)
     */
    public static final int COUNTDOWN_LENGTH = 2;

    /**
     * An annotation denoting that the field is not just a random integer
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({DATE_CREATED, TIME_LEFT, COUNTDOWN_LENGTH})
    public @interface SortOption {

    }
}