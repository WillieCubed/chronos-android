package com.craft.apps.countdowns.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

/**
 * If the current time is after this instant, the result will be negative.
 */
fun Instant.durationFromNow(): Duration = (this - Clock.System.now())

/**
 * Return the number of seconds until now, rounding down.
 *
 * If this is negative, that means this instant has already passed.
 */
fun Instant.secondsUntilNow(): Long = durationFromNow().inWholeSeconds

/**
 * Return the number of minutes until now, rounding down.
 *
 * If this is negative, that means this instant has already passed.
 */
fun Instant.minutesUntilNow(): Long = durationFromNow().inWholeMinutes

/**
 * Return the number of hours until now, rounding down.
 *
 * If this is negative, that means this instant has already passed.
 */
fun Instant.hoursUntilNow(): Long = durationFromNow().inWholeHours

/**
 * Return the number of days until now, rounding down.
 *
 * If this is negative, that means this instant has already passed.
 */
fun Instant.daysUntilNow(): Long = durationFromNow().inWholeDays

// TODO: Figure out whether week and month approximations are suitable for UX

/**
 * Return the number of weeks until now, rounding down.
 *
 * If this is negative, that means this instant has already passed.
 */
fun Instant.weeksUntilNow(): Long = durationFromNow().inWholeDays / 7

/**
 * Return the number of months until now, rounding down.
 *
 * If this is negative, that means this instant has already passed.
 */
fun Instant.monthsUntilNow(): Long = durationFromNow().inWholeDays / 30

