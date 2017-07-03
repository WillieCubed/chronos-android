package com.craft.apps.countdowns;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (1/14/17)
 */
public class CountdownCalculator {

    static long getMillisecondsUntil(long endTime) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return endTime - currentTime;
    }

    static String getUserReadableTime(long unixTime, Locale locale) {
        SimpleDateFormat format = new SimpleDateFormat("", locale);
        return format.format(new Date(unixTime));
    }

    static String getUserReadableTime(long unixTime) {
        return getUserReadableTime(unixTime, Locale.US);
    }
}
