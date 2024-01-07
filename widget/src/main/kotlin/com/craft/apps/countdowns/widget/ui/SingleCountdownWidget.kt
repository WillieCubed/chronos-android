package com.craft.apps.countdowns.widget.ui

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays information for a single [Countdown] on the app launcher.
 */
class SingleCountdownWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val context = LocalContext.current.applicationContext
//            val repository: WidgetCountdownRepository = EntryPoints.get(context, En)
            Text("2 days until birthday")
        }
    }
}

@AndroidEntryPoint
class SingleCountdownWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SingleCountdownWidget()

}