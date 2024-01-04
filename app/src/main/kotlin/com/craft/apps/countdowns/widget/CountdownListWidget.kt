package com.craft.apps.countdowns.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.craft.apps.countdowns.R

/**
 * Lists [Countdown]s in an app widget format on the home screen
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (6/25/17)
 */
class CountdownListWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            // Construct the RemoteViews object
            val remoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_countdown_list
            )
            val intent = Intent(context, CountdownListWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)))
            remoteViews.setRemoteAdapter(R.id.list_countdowns_widget, intent)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
}
