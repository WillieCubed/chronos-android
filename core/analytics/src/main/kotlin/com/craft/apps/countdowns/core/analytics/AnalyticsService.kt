package com.craft.apps.countdowns.core.analytics

/**
 * A generic analytics service for recording user behavior.
 */
interface AnalyticsService {
    fun logCreation(countdownId: String)
    fun logSelection(countdownId: String)

    // TODO: Log Countdown edits
    fun logDeletion(countdownId: String)
    fun logSingleWidgetAddition()
    fun logSingleWidgetRemoval()
    fun logSingleWidgetEngagement()
    fun logScreenVisit(screenId: ScreenId, activityName: String = "MainActivity")

    enum class ScreenId(val screenName: String) {
        HOME("screen_home"),
        COUNTDOWN_DETAILS("screen_countdown_details"),
        COUNTDOWN_WIDGET_CONFIGURE("screen_widget_configure")
    }
}