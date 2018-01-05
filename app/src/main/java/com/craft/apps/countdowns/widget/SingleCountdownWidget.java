package com.craft.apps.countdowns.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.craft.apps.countdowns.CountdownDeepLinkActivity;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.analytics.CountdownAnalytics;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.craft.apps.countdowns.common.util.IntentUtils.ACTION_VIEW_COUNTDOWN_DETAILS;
import static com.craft.apps.countdowns.common.util.IntentUtils.ARG_COUNTDOWN_ID;
import static com.craft.apps.countdowns.widget.WidgetManager.deleteCountdownIdPreference;
import static com.craft.apps.countdowns.widget.WidgetManager.loadCountdownIdPreference;

/**
 * An app widget that displays the number of days until the completion of a {@link Countdown}
 * This app widget is configurable in the {@link SingleCountdownWidgetConfigureActivity}.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class SingleCountdownWidget extends AppWidgetProvider {

    /**
     * A magic number request code used to generate a {@link PendingIntent} for widget click events
     */
    public static final int RC_VIEW_COUNTDOWN_DETAILS = 1;

    private static final String TAG = SingleCountdownWidget.class.getSimpleName();

    /**
     * A mapping of row and column counts to properly formatted layout resource
     * Starts with row 1, column 2
     */
    private static final int[][] WIDGET_LAYOUTS = new int[][]{
            {
                    R.layout.widget_individual_countdown_two_column_one_row,
                    R.layout.widget_individual_countdown_two_column_two_row,
                    R.layout.widget_individual_countdown_two_column_three_row,
                    R.layout.widget_individual_countdown_two_column_four_row,
                    R.layout.widget_individual_countdown_two_column_five_row
            },
            {
                    R.layout.widget_individual_countdown_three_column_one_row,
                    R.layout.widget_individual_countdown_three_column_two_row,
                    R.layout.widget_individual_countdown_three_column_three_row,
                    R.layout.widget_individual_countdown_three_column_four_row,
                    R.layout.widget_individual_countdown_three_column_five_row
            },
            {
                    R.layout.widget_individual_countdown_four_column_one_row,
                    R.layout.widget_individual_countdown_four_column_two_row,
                    R.layout.widget_individual_countdown_four_column_three_row,
                    R.layout.widget_individual_countdown_four_column_four_row,
                    R.layout.widget_individual_countdown_four_column_five_row
            },
            {
                    // This is deliberate
                    R.layout.widget_individual_countdown_five_column_one_row,
                    R.layout.widget_individual_countdown_five_column_two_row,
                    R.layout.widget_individual_countdown_five_column_three_row,
                    R.layout.widget_individual_countdown_five_column_four_row,
                    R.layout.widget_individual_countdown_five_column_five_row
            },
    };

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        DatabaseReference countdownReference = loadCountdownIdPreference(context, appWidgetId);
        // Construct the RemoteViews object
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_individual_countdown_two_column_one_row);
        Log.v(TAG, "updateAppWidget: RemoteView constructed");

        if (countdownReference != null) {
            countdownReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot == null) {
                        Toast.makeText(context, R.string.label_error_countdown_not_exist,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        updateWidgetView(context, appWidgetManager, appWidgetId, remoteViews,
                                dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, databaseError.toException());
                }

            });

        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static void updateWidgetView(Context context, AppWidgetManager widgetManager,
                                         int appWidgetId, RemoteViews remoteViews, DataSnapshot snapshot) {
        if (snapshot == null) {
            Log.i(TAG, "updateWidgetView: Countdown has been deleted; displaying fallback");
            remoteViews.setTextViewText(R.id.appwidget_countdown_title,
                    context.getString(R.string.appwidget_countdown_label_dne));
            remoteViews.setTextViewText(R.id.appwidget_countdown_caption,
                    context.getString(R.string.appwidget_countdown_label_remove));
            widgetManager.updateAppWidget(appWidgetId, remoteViews);
            return;
        }
        Long unixEnd = snapshot.child("finishTime").getValue(Long.class);
        String titleData = snapshot.child("title").getValue(String.class);
        String formattedTitle = context.getString(
                R.string.appwidget_countdown_caption, titleData);

        int daysUntil = UnitsFormatter.getUnitsUntil(unixEnd, UnitsFormatter.DAYS);
        String daysString = context.getResources().getQuantityString(R.plurals.countdown_unit_days,
                daysUntil, daysUntil);
        if (daysUntil <= 0) {
            remoteViews.setTextViewText(R.id.appwidget_countdown_title,
                    context.getString(R.string.appwidget_countdown_label_fulfillment));
            remoteViews.setTextViewText(R.id.appwidget_countdown_caption,
                    context.getString(R.string.appwidget_countdown_label_remove));
        } else {
            Log.d(TAG, "onDataChange: " + daysString);
            remoteViews.setTextViewText(R.id.appwidget_countdown_title, daysString);
            remoteViews.setTextViewText(R.id.appwidget_countdown_caption, formattedTitle);
        }

        Intent intent = new Intent(context, CountdownDeepLinkActivity.class)
                .setAction(ACTION_VIEW_COUNTDOWN_DETAILS)
                .putExtra(ARG_COUNTDOWN_ID, snapshot.getKey());
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                RC_VIEW_COUNTDOWN_DETAILS, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent);
        widgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    /**
     * Returns number of cells needed for given dimension of the widget.
     *
     * @param size Widget size in dp.
     * @return Size in number of cells.
     */
    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size) {
            n++;
        }
        return n - 1;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            deleteCountdownIdPreference(context, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        CountdownAnalytics.getInstance(context).logSingleWidgetAddition();
    }

    @Override
    public void onDisabled(Context context) {
        CountdownAnalytics.getInstance(context).logSingleWidgetRemoval();
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        Log.v(TAG, "Dimensions changed; reconfiguring layout with bundle: " + newOptions);

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

        appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context, minWidth, minHeight));
    }

    /**
     * Determines appropriate layout resource based on width provided.
     */
    @NonNull
    private RemoteViews getRemoteViews(Context context, int minWidth, int minHeight) {
        int rows = getCellsForSize(minHeight);
        int columns = getCellsForSize(minWidth);
        // TODO: 7/3/17 Add dynamic layouts
//        return new RemoteViews(context.getPackageName(), WIDGET_LAYOUTS[rows][columns - 1]);
        return new RemoteViews(context.getPackageName(), WIDGET_LAYOUTS[0][0]);
    }
}
