package com.craft.apps.countdowns.ui.util

import android.content.Context
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.core.ui.R
import com.craft.apps.countdowns.util.daysUntilNow
import com.craft.apps.countdowns.util.hoursUntilNow
import com.craft.apps.countdowns.util.minutesUntilNow
import com.craft.apps.countdowns.util.secondsUntilNow

enum class CountdownDisplayStyle {
    /**
     * Change depending on the most significant duration
     *
     * e.g. 2 days, 11 hours, 3 minutes
     */
    DYNAMIC,

    /**
     * Comically big.
     *
     * e.g. 468,000 seconds
     */
    LEAST_SIGNIFICANT,

    /**
     * Display a duration only up to hours and minutes.
     */
    SHORT_TERM,
}

/**
 * @param short If true, omit the label after the units (e.g. only "12 days" instead of "12 days until now")
 */
fun Countdown.getFormattedTimeLeft(
    context: Context,
    style: CountdownDisplayStyle,
    short: Boolean = false,
): String {
    // TODO: Finish me
    val instant = this.expiration
    val daysLeft = instant.daysUntilNow()
    if (daysLeft < 1) {
        val hoursLeft = instant.hoursUntilNow()
        if (hoursLeft < 1) {
            val minutesLeft = instant.minutesUntilNow()
            if (short) {
                context.getString(R.string.minutes_until_now_short, minutesLeft)
            } else {
                context.getString(R.string.minutes_until_now_long, minutesLeft)
            }
        }
    }
    val secondsLeft = instant.secondsUntilNow()
    return ""
}
