package com.craft.apps.countdowns.common.util;

/**
 * @author willie
 * @version 1.0.0
 * @since 6/24/17
 */
public class Preconditions {

    public static <T> T checkNotNull(T o) {
        if (o == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
        return o;
    }
}
