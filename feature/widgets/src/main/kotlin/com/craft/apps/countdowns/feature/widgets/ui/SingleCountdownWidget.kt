package com.craft.apps.countdowns.feature.widgets.ui

import android.content.Context
import androidx.glance.GlanceId
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
//            val repository: WidgetCountdownRepository =
//                EntryPoints.get(context, WidgetCountdownRepositoryFactory::class.java)
//                    .repositoryFactory()

            CountdownsWidgetTheme {
                Text("2 days until birthday")
            }
        }
    }

//    @EntryPoint
//    @InstallIn(SingletonComponent::class)
//    interface WidgetCountdownRepositoryFactory {
//        fun repositoryFactory(): WidgetCountdownRepository
//    }
}

@AndroidEntryPoint
class SingleCountdownWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SingleCountdownWidget()

}