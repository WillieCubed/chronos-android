package com.craft.apps.countdowns.core.data.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.DurationUnit

/**
 * A countdown wrapper for an instant in time.
 *
 * This is the primary data type for the app.
 */
data class Countdown(
    /**
     * This countdown's unique identifier
     */
    val id: Int,

    /**
     * The title for this countdown.
     */
    val label: String,

    /**
     * The instant this countdown expires.
     */
    val timestamp: Instant,
) {
    /**
     * A value indicating how salient this countdown is.
     */
    val importance: Int
        get() {
            return (timestamp - Clock.System.now()).toInt(DurationUnit.DAYS)
        }
}

/**
 * Converts a countdown's timestamp to a human-readable representation.
 */
fun Instant.formatted(): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return formatter.format(
        this.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    )
}
