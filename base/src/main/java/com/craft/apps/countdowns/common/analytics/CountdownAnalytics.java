package com.craft.apps.countdowns.common.analytics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * A utility class that sends customized analytics events to the app backend.
 *
 * This uses Google Analytics for Firebase as the backend.
 *
 * @author willie
 * @version 1.0.0
 * @see Event
 * @see Param
 * @since v1.0.0 (5/29/17)
 */
public class CountdownAnalytics {

    private FirebaseAnalytics mAnalytics;

    private CountdownAnalytics(Context context) {
        mAnalytics = FirebaseAnalytics.getInstance(context);
    }

    /**
     * Provides a semantically friendly way to get an instance of this class
     *
     * @param context A context used for analytics
     * @return A new CountdownAnalytics instance
     */
    @NonNull
    public static CountdownAnalytics getInstance(Context context) {
        return new CountdownAnalytics(context);
    }

    /**
     * Sends a {@link Event#COUNTDOWN_CREATED} event to analytics
     *
     * @param countdownId The database id for the {@link Countdown} to log
     */
    public void logCreation(String countdownId) {
        Bundle args = new Bundle();
        args.putString(FirebaseAnalytics.Param.ITEM_ID, countdownId);
        mAnalytics.logEvent(Event.COUNTDOWN_CREATED, args);
    }

    /**
     * Sends a {@link Event#COUNTDOWN_SELECTED} event to analytics
     *
     * @param countdownId The database id for the {@link Countdown} to log
     */
    public void logSelection(String countdownId) {
        Bundle args = new Bundle();
        args.putString(FirebaseAnalytics.Param.ITEM_ID, countdownId);
        args.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "main_list_item");
        mAnalytics.logEvent(Event.COUNTDOWN_SELECTED, args);
    }

    /**
     * Sends a {@link Event#COUNTDOWN_DELETED} event to analytics
     *
     * @param countdownId The database id for the {@link Countdown} to log
     */
    public void logDeletion(String countdownId) {
        Bundle args = new Bundle();
        args.putString(FirebaseAnalytics.Param.ITEM_ID, countdownId);
        mAnalytics.logEvent(Event.COUNTDOWN_DELETED, args);
    }

    /**
     * Sends a {@link Event#WIDGET_SINGLE_ADDED} event to analytics
     */
    public void logSingleWidgetAddition() {
        // TODO: 6/24/17 Add more useful analytics for widgets
        mAnalytics.logEvent(Event.WIDGET_SINGLE_ADDED, new Bundle());
    }

    /**
     * Sends a {@link Event#WIDGET_SINGLE_REMOVED} event to analytics
     */
    public void logSingleWidgetRemoval() {
        // TODO: 6/24/17 Add more useful analytics for widgets
        mAnalytics.logEvent(Event.WIDGET_SINGLE_REMOVED, new Bundle());
    }

    /**
     * Sends a {@link Event#WIDGET_SINGLE_SELECTED} event to analytics
     */
    public void logSingleWidgetEngagement() {
        mAnalytics.logEvent(Event.WIDGET_SINGLE_SELECTED, new Bundle());
    }

    /**
     * A class of constants denoting analytics events related to this app
     *
     * @see Param
     */
    static final class Event {

        /**
         * Indicates that a {@link Countdown} has been created and put to the database
         */
        static final String COUNTDOWN_CREATED = "engagement_countdown_creation";

        /**
         * Indicates that a {@link Countdown} has been deleted from the database
         */
        static final String COUNTDOWN_DELETED = "countdown_deletion";

        /**
         * Indicates that a {@link Countdown} has been selected in a list
         */
        static final String COUNTDOWN_SELECTED = "engagement_countdown_selection";

        /**
         * Indicates that a SingleCountdownWidget has been added to the home screen
         */
        static final String WIDGET_SINGLE_ADDED = "widget_single_added";

        /**
         * Indicates that a SingleCountdownWidget has been removed from the home screen
         */
        static final String WIDGET_SINGLE_REMOVED = "widget_single_removed";

        static final String WIDGET_SINGLE_SELECTED = "engagement_widget_single_select";

        static final String ENGAGEMENT_DEEP_LINK_WEB = "engagement_deep_link_web";

        private Event() {
        }
    }

    /**
     * A class of constants denoting parameters for analytics events
     *
     * @see Event
     */
    static final class Param {

        private Param() {
        }
    }
}
