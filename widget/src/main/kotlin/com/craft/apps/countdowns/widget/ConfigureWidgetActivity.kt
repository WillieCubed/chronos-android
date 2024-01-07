package com.craft.apps.countdowns.widget

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.craft.apps.countdowns.theme.CountdownsTheme
import com.craft.apps.countdowns.widget.ui.ConfigureWidgetScreen
import com.craft.apps.countdowns.widget.ui.ConfigureWidgetViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * The activity that allows a user to configure any of the countdown widgets.
 */
@AndroidEntryPoint
class ConfigureWidgetActivity : ComponentActivity() {
    private val viewModel by viewModels<ConfigureWidgetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = GlanceAppWidgetManager(this)
        val glanceId = manager.getGlanceIdBy(intent)
        if (glanceId == null) {
            finish()
            return
        }
        val widgetId = manager.getAppWidgetId(glanceId)

        with(intent) {
            val widgetType = getStringExtra(WidgetExtras.WIDGET_TYPE)
            if (widgetType == null) {
                // It's null, so this isn't first-type setup
                Log.e(
                    this@ConfigureWidgetActivity::class.simpleName,
                    "Given widget type is unexpectedly null"
                )
                return@with
            }
            when (widgetType) {
                WidgetType.DETAIL -> {
                    // If a countdownId is provided, select it automatically and finish
                    val countdownId =
                        getIntExtra(WidgetExtras.COUNTDOWN_ID, WidgetExtras.DEFAULT_WIDGET_ID)
                    if (countdownId != WidgetExtras.DEFAULT_WIDGET_ID) {
                        viewModel.selectCountdown(countdownId, widgetId)
                        finish()
                        return
                    }
                    // Otherwise, let user configure the chosen countdown
                }

                WidgetType.LIST -> {
                    // TODO: Implement list widget configuration
                }

                else -> {
                    Log.e(
                        this@ConfigureWidgetActivity::class.simpleName,
                        "Unknown widgetType $widgetId"
                    )
                    finish()
                    return
                }
            }

        }

        setContent {
            CountdownsTheme {
                ConfigureWidgetScreen()
            }
        }
    }
}

