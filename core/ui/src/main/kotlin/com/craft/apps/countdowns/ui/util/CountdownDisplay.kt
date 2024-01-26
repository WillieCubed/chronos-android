package com.craft.apps.countdowns.ui.util

import android.content.Context
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.core.ui.R
import com.craft.apps.countdowns.util.daysUntilNow
import com.craft.apps.countdowns.util.hoursUntilNow
import com.craft.apps.countdowns.util.isAfterNow
import com.craft.apps.countdowns.util.minutesUntilNow
import com.craft.apps.countdowns.util.secondsUntilNow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

fun Instant.toJavaLocal() = toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()

fun Instant.formattedLongDate(): String =
    this.toJavaLocal().toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

fun Instant.formattedShortDate(): String =
    this.toJavaLocal().toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))

fun Instant.formattedShortTime(): String =
    this.toJavaLocal().toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

fun LocalDateTime.formattedShortTime(): String =
    this.toJavaLocalDateTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

// TODO: Remove year if the countdown is in the current year
val Countdown.formattedExpirationDate: String
    get() = expiration.formattedLongDate()

val Countdown.formattedExpirationTime: String
    get() = expiration.formattedShortTime()

val Countdown.isExpired: Boolean
    get() = expiration.isAfterNow()