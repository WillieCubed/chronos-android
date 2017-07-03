package com.craft.apps.countdowns.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A class that handles widget creation, deletion, and more.
 */
public final class WidgetManager {

    public static final int RC_WIDGET_ADD = 4;
    private static final String TAG = WidgetManager.class.getSimpleName();
    private static final String PREFERENCE_NAME = "com.craft.apps.countdown.SingleCountdownWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private WidgetManager() {
        // There's no point to instantiate this.
    }

    // Write the prefix to the SharedPreferences object for this widget
    public static void saveCountdownIdPreference(Context context, int appWidgetId,
            String countdownId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, countdownId);
        Log.v(TAG, "Saving widget ID");
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static DatabaseReference loadCountdownIdPreference(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, 0);
        String countdownId = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (countdownId != null) {
            Log.v(TAG, "Loading countdown widget ID preference for " + countdownId);
            return FirebaseDatabase.getInstance().getReference().child("countdowns")
                    .child(countdownId);
        } else {
            return null;
        }
    }

    /**
     * Removes a widget's ID from the system's Shared Preferences
     *
     * @param context The context to access the SharedPreferences
     * @param appWidgetId The ID creator the app widget to delete
     */
    public static void deleteCountdownIdPreference(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}
