package com.craft.apps.countdowns.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.craft.apps.countdowns.R
import com.craft.apps.countdowns.analytics.CountdownsAnalyticsService
import com.craft.apps.countdowns.common.format.UnitsFormatter
import com.craft.apps.countdowns.common.util.IntentUtils
import com.craft.apps.countdowns.core.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

/**
 * An app widget that displays the number of days until the completion of a [com.craft.apps.countdowns.core.data.repository.Countdown]
 * This app widget is configurable in the [SingleCountdownWidgetConfigureActivity].
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (3/18/17)
 */
class SingleCountdownWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            WidgetManager.deleteCountdownIdPreference(context, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {
        CountdownsAnalyticsService.getInstance(context).logSingleWidgetAddition()
    }

    override fun onDisabled(context: Context) {
        CountdownsAnalyticsService.getInstance(context).logSingleWidgetRemoval()
    }

    override fun onAppWidgetOptionsChanged(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle
    ) {
        Log.v(TAG, "Dimensions changed; reconfiguring layout with bundle: $newOptions")
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context, minWidth, minHeight))
    }

    /**
     * Determines appropriate layout resource based on width provided.
     */
    private fun getRemoteViews(context: Context, minWidth: Int, minHeight: Int): RemoteViews {
        val rows = getCellsForSize(minHeight)
        val columns = getCellsForSize(minWidth)
        // TODO: 7/3/17 Add dynamic layouts
//        return new RemoteViews(context.getPackageName(), WIDGET_LAYOUTS[rows][columns - 1]);
        return RemoteViews(context.packageName, WIDGET_LAYOUTS[0][0])
    }

    companion object {
        /**
         * A magic number request code used to generate a [PendingIntent] for widget click events
         */
        private const val RC_VIEW_COUNTDOWN_DETAILS = 1
        private val TAG = SingleCountdownWidget::class.java.simpleName

        /**
         * A mapping of row and column counts to properly formatted layout resource
         * Starts with row 1, column 2
         */
        private val WIDGET_LAYOUTS = arrayOf(
            intArrayOf(
                R.layout.widget_individual_countdown_two_column_one_row,
                R.layout.widget_individual_countdown_two_column_two_row,
                R.layout.widget_individual_countdown_two_column_three_row,
                R.layout.widget_individual_countdown_two_column_four_row,
                R.layout.widget_individual_countdown_two_column_five_row
            ), intArrayOf(
                R.layout.widget_individual_countdown_three_column_one_row,
                R.layout.widget_individual_countdown_three_column_two_row,
                R.layout.widget_individual_countdown_three_column_three_row,
                R.layout.widget_individual_countdown_three_column_four_row,
                R.layout.widget_individual_countdown_three_column_five_row
            ), intArrayOf(
                R.layout.widget_individual_countdown_four_column_one_row,
                R.layout.widget_individual_countdown_four_column_two_row,
                R.layout.widget_individual_countdown_four_column_three_row,
                R.layout.widget_individual_countdown_four_column_four_row,
                R.layout.widget_individual_countdown_four_column_five_row
            ), intArrayOf( // This is deliberate
                R.layout.widget_individual_countdown_five_column_one_row,
                R.layout.widget_individual_countdown_five_column_two_row,
                R.layout.widget_individual_countdown_five_column_three_row,
                R.layout.widget_individual_countdown_five_column_four_row,
                R.layout.widget_individual_countdown_five_column_five_row
            )
        )

        @JvmStatic
        fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int
        ) {
            val countdownReference = WidgetManager.loadCountdownIdPreference(context, appWidgetId)
            // Construct the RemoteViews object
            val remoteViews = RemoteViews(
                context.packageName, R.layout.widget_individual_countdown_two_column_one_row
            )
            Log.v(TAG, "updateAppWidget: RemoteView constructed")
            countdownReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    updateWidgetView(
                        context, appWidgetManager, appWidgetId, remoteViews, dataSnapshot
                    )
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, databaseError.toException())
                }
            })
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

        private fun updateWidgetView(
            context: Context,
            widgetManager: AppWidgetManager,
            appWidgetId: Int,
            remoteViews: RemoteViews,
            snapshot: DataSnapshot?
        ) {
            if (snapshot == null) {
                Log.i(TAG, "updateWidgetView: Countdown has been deleted; displaying fallback")
                remoteViews.setTextViewText(
                    R.id.appwidget_countdown_title,
                    context.getString(R.string.appwidget_countdown_label_dne)
                )
                remoteViews.setTextViewText(
                    R.id.appwidget_countdown_caption,
                    context.getString(R.string.appwidget_countdown_label_remove)
                )
                widgetManager.updateAppWidget(appWidgetId, remoteViews)
                return
            }
            val unixEnd = snapshot.child("finishTime").getValue(Long::class.java)
            val titleData = snapshot.child("title").getValue(String::class.java)
            val formattedTitle = context.getString(
                R.string.appwidget_countdown_caption, titleData
            )
            val daysUntil = UnitsFormatter.getUnitsUntil(unixEnd!!, UnitsFormatter.DAYS)
            val daysString = context.resources.getQuantityString(
                R.plurals.countdown_unit_days, daysUntil, daysUntil
            )
            if (daysUntil <= 0) {
                remoteViews.setTextViewText(
                    R.id.appwidget_countdown_title,
                    context.getString(R.string.appwidget_countdown_label_fulfillment)
                )
                remoteViews.setTextViewText(
                    R.id.appwidget_countdown_caption,
                    context.getString(R.string.appwidget_countdown_label_remove)
                )
            } else {
                Log.d(TAG, "onDataChange: $daysString")
                remoteViews.setTextViewText(R.id.appwidget_countdown_title, daysString)
                remoteViews.setTextViewText(R.id.appwidget_countdown_caption, formattedTitle)
            }
            val intent = Intent(
                context,
                MainActivity::class.java
            ).setAction(IntentUtils.ACTION_VIEW_COUNTDOWN_DETAILS)
                .putExtra(IntentUtils.ARG_COUNTDOWN_ID, snapshot.key)
            val pendingIntent = PendingIntent.getActivity(
                context,
                RC_VIEW_COUNTDOWN_DETAILS, intent, PendingIntent.FLAG_IMMUTABLE,
            )
            remoteViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            widgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

        /**
         * Returns number of cells needed for given dimension of the widget.
         *
         * @param size Widget size in dp.
         * @return Size in number of cells.
         */
        private fun getCellsForSize(size: Int): Int {
            var n = 2
            while (70 * n - 30 < size) {
                n++
            }
            return n - 1
        }
    }
}
