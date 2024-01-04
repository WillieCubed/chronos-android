package com.craft.apps.countdowns.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.craft.apps.countdowns.R
import com.craft.apps.countdowns.common.format.UnitsFormatter
import com.craft.apps.countdowns.common.model.Countdown

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (6/28/17)
 */
class CountdownListWidgetService : RemoteViewsService() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return CountdownListRemoteViewsFactory(applicationContext, intent)
    }

    private class CountdownListRemoteViewsFactory(private val context: Context, intent: Intent?) :
        RemoteViewsFactory {
        private val countdownList: List<Countdown> = ArrayList()
        override fun onCreate() {}
        override fun onDataSetChanged() {}
        override fun onDestroy() {}
        override fun getCount(): Int {
            return countdownList.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val remoteViews =
                RemoteViews(context.packageName, R.layout.viewholder_countdown_list_item)
            val finishTime = getItem(position).finishTime
            // TODO: 6/28/17 Allow specification of unit type
            val units = UnitsFormatter.getUnitsUntil(finishTime, UnitsFormatter.DAYS)
            val pluralText = context.resources
                .getQuantityString(R.plurals.countdown_unit_days, units, units)
            remoteViews.setTextViewText(R.id.countdown_counter, pluralText)
            remoteViews.setTextViewText(R.id.countdown_title, getItem(position).title)
            return remoteViews
        }

        private fun getItem(index: Int): Countdown {
            return countdownList[index]
        }

        override fun getLoadingView(): RemoteViews {
            return RemoteViews(context.packageName, R.layout.viewholder_countdown_list_item)
        }

        override fun getViewTypeCount(): Int {
            return 0
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun hasStableIds(): Boolean {
            return true
        }
    }

    companion object {
        const val EXTRA_USER_ID = "com.craft.apps.countdowns.extra.USER_ID"
        private val TAG = CountdownListWidgetService::class.java.simpleName
    }
}
