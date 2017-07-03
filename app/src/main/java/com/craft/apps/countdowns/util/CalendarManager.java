package com.craft.apps.countdowns.util;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.craft.apps.countdowns.common.model.Countdown;

/**
 * @author willie
 * @version 1.0.0
 * @since 5/14/17
 */
public class CalendarManager {

    private static final String TAG = CalendarManager.class.getSimpleName();

    public static void addCountdown(Context context, Countdown countdown) {
        long calID = 3;
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, countdown.getStartTime());
        values.put(Events.DTEND, countdown.getFinishTime());
        values.put(Events.TITLE, countdown.getTitle());
        values.put(Events.DESCRIPTION, countdown.getDescription());
        values.put(Events.CALENDAR_ID, calID);
        values.put(Events.EVENT_TIMEZONE, countdown.getTimezone());
        if (ActivityCompat.checkSelfPermission(context, permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Do this because countdown upload is done elsewhere
        } else {
            Log.d(TAG, "addCountdown: Adding countdown to system calendar");
//            Uri uri = contentResolver.insert(Events.CONTENT_URI, values);
//            long eventID = Long.parseLong(uri.getLastPathSegment());
        }
    }
}
