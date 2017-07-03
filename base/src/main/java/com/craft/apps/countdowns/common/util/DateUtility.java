package com.craft.apps.countdowns.common.util;

import org.joda.time.DateTime;

/**
 * A utility class that abstracts fetching date and time
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (6/29/17)
 */
public class DateUtility {

    private final DateTime mDateTime;

    public DateUtility() {
        mDateTime = new DateTime();
    }

    public DateUtility(int year, int month, int day, int hour, int minute) {
        mDateTime = new DateTime(year, month, day, hour, minute);
    }

    public int getYear() {
        return mDateTime.getYear();
    }

    public int getDayOfMonth() {
        return mDateTime.getDayOfMonth();
    }

    public int getMonthOfYear() {
        return mDateTime.getMonthOfYear();
    }

    public int getHourOfDay() {
        return mDateTime.getHourOfDay();
    }

    public int getMinuteOfHour() {
        return mDateTime.getMinuteOfHour();
    }

    public DateTime asDateTime() {
        return mDateTime;
    }

    public long getEndMillis() {
        return mDateTime.getMillis();
    }
}
