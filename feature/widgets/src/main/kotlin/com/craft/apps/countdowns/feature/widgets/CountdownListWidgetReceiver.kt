package com.craft.apps.countdowns.feature.widgets

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.craft.apps.countdowns.core.coroutines.IoDispatcher
import com.craft.apps.countdowns.feature.widgets.data.WidgetCountdownRepository
import com.craft.apps.countdowns.feature.widgets.ui.CountdownListWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Lists [Countdown]s in an app widget format on the home screen
 *
 * @author willie
 * @version 2.0.0
 * @since v1.0.0 (6/25/17)
 */
@AndroidEntryPoint
class CountdownListWidgetReceiver @Inject constructor(
) : GlanceAppWidgetReceiver() {
    @Inject
    @IoDispatcher
    lateinit var coroutineDispatcher: CoroutineDispatcher

    @Inject
    lateinit var countdownRepository: WidgetCountdownRepository

    override val glanceAppWidget: GlanceAppWidget = CountdownListWidget(countdownRepository)

    override fun onEnabled(context: Context) {
        CoroutineScope(coroutineDispatcher).launch {
            // TODO: Use WorkManager to start automatic updates
        }
    }
}
