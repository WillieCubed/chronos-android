package com.craft.apps.countdowns.feature.widgets.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme
import androidx.glance.material3.ColorProviders
import com.craft.apps.countdowns.ui.theme.DarkColorScheme
import com.craft.apps.countdowns.ui.theme.LightColorScheme

/**
 * A custom color configuration object for Jetpack Glance.
 */
object CountdownsGlanceColorScheme {

    val colors = ColorProviders(
        light = LightColorScheme,
        dark = DarkColorScheme,
    )
}

/**
 * A theme for Countdowns app widgets that is compatible with Jetpack Glance.
 *
 * @see [com.craft.apps.countdowns.theme.CountdownsTheme] for the normal, non-widget theme
 */
@Composable
fun CountdownsWidgetTheme(
    content: @Composable () -> Unit
) {
    val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        GlanceTheme.colors
    } else {
        CountdownsGlanceColorScheme.colors
    }
    GlanceTheme(
        colors = colors,
        content = content,
    )
}