package com.craft.apps.countdowns.analytics

import android.content.Context
import android.os.Bundle
import com.craft.apps.countdowns.analytics.CountdownsAnalyticsService.Event
import com.craft.apps.countdowns.analytics.CountdownsAnalyticsService.Param
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

/**
 * A utility class that sends customized analytics events to the app backend.
 *
 * This uses Google Analytics for Firebase as the backend.
 *
 * @author willie
 * @version 1.0.0
 * @see Event
 *
 * @see Param
 *
 * @since v1.0.0 (5/29/17)
 */
class CountdownsAnalyticsService @Inject constructor(context: Context) : AnalyticsService {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    /**
     * Sends a [Event.COUNTDOWN_CREATED] event to analytics
     *
     * @param countdownId The database id for the [Countdown] to log
     */
    override fun logCreation(countdownId: String) {
        val args = Bundle()
        args.putString(FirebaseAnalytics.Param.ITEM_ID, countdownId)
        firebaseAnalytics.logEvent(Event.COUNTDOWN_CREATED, args)
    }

    /**
     * Sends a [Event.COUNTDOWN_SELECTED] event to analytics
     *
     * @param countdownId The database id for the [Countdown] to log
     */
    override fun logSelection(countdownId: String) {
        val args = Bundle()
        args.putString(FirebaseAnalytics.Param.ITEM_ID, countdownId)
        args.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "main_list_item")
        firebaseAnalytics.logEvent(Event.COUNTDOWN_SELECTED, args)
    }

    /**
     * Sends a [Event.COUNTDOWN_DELETED] event to analytics
     *
     * @param countdownId The database id for the [Countdown] to log
     */
    override fun logDeletion(countdownId: String) {
        val args = Bundle()
        args.putString(FirebaseAnalytics.Param.ITEM_ID, countdownId)
        firebaseAnalytics.logEvent(Event.COUNTDOWN_DELETED, args)
    }

    /**
     * Sends a [Event.WIDGET_SINGLE_ADDED] event to analytics
     */
    override fun logSingleWidgetAddition() {
        // TODO: 6/24/17 Add more useful analytics for widgets
        firebaseAnalytics.logEvent(Event.WIDGET_SINGLE_ADDED, Bundle())
    }

    /**
     * Sends a [Event.WIDGET_SINGLE_REMOVED] event to analytics
     */
    override fun logSingleWidgetRemoval() {
        // TODO: 6/24/17 Add more useful analytics for widgets
        firebaseAnalytics.logEvent(Event.WIDGET_SINGLE_REMOVED, Bundle())
    }

    /**
     * Sends a [Event.WIDGET_SINGLE_SELECTED] event to analytics
     */
    override fun logSingleWidgetEngagement() {
        firebaseAnalytics.logEvent(Event.WIDGET_SINGLE_SELECTED, Bundle())
    }

    override fun logScreenVisit(screenId: AnalyticsService.ScreenId, activityName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenId.screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, activityName)
        }
    }

    /**
     * A class of constants denoting analytics events related to this app
     *
     * @see Param
     */
    internal object Event {
        /**
         * Indicates that a [Countdown] has been created and put to the database
         */
        const val COUNTDOWN_CREATED = "engagement_countdown_creation"

        /**
         * Indicates that a [Countdown] has been deleted from the database
         */
        const val COUNTDOWN_DELETED = "countdown_deletion"

        /**
         * Indicates that a [Countdown] has been selected in a list
         */
        const val COUNTDOWN_SELECTED = "engagement_countdown_selection"

        /**
         * Indicates that a SingleCountdownWidget has been added to the home screen
         */
        const val WIDGET_SINGLE_ADDED = "widget_single_added"

        /**
         * Indicates that a SingleCountdownWidget has been removed from the home screen
         */
        const val WIDGET_SINGLE_REMOVED = "widget_single_removed"
        const val WIDGET_SINGLE_SELECTED = "engagement_widget_single_select"
        const val ENGAGEMENT_DEEP_LINK_WEB = "engagement_deep_link_web"
    }

    /**
     * A class of constants denoting parameters for analytics events
     *
     * @see Event
     */
    internal class Param private constructor()
    companion object {
        /**
         * Provides a semantically friendly way to get an instance of this class
         *
         * @param context A context used for analytics
         * @return A new CountdownAnalytics instance
         */
        fun getInstance(context: Context): CountdownsAnalyticsService {
            return CountdownsAnalyticsService(context)
        }
    }
}
