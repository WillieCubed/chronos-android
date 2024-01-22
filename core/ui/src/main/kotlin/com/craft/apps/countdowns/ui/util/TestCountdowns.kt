package com.craft.apps.countdowns.ui.util

import com.craft.apps.countdowns.core.model.Countdown
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * A list of sample countdowns.
 *
 * This can be used for mocking data or previews.
 */
val testCountdowns = mutableListOf(
    Countdown(
        1,
        "Lawrence Announcement",
        Instant.parse("2024-01-22T12:00:00-04"),
        Clock.System.now(),
        "These are some notes.",
        true,
    ),
    Countdown(
        2,
        "Someone's Birthday",
        Instant.parse("2024-06-16T00:00:00-07"),
        Clock.System.now(),
        "These are some notes.",
        false,
    ),
    Countdown(
        3,
        "The Best Day of the Year",
        Instant.parse("2024-12-31T00:00:00Z"),
        Clock.System.now(),
        "These are some notes.",
        false,
    ),
)