package com.craft.apps.countdowns.common;

import com.craft.apps.countdowns.common.model.Countdown;
import java.util.TimeZone;
import org.joda.time.DateTime;

// TODO: 6/29/17 Use Java 8 time APIs

/**
 * A factory class for temporarily storing values relevant for {@link Countdown} creation.
 *
 * @author willie
 * @version 1.0.0
 * @see Countdown
 * @since 1.0.0 (6/29/17)
 */
public class CountdownCreator {

    private String mCountdownTitle;

    private String mCountdownDescription;

    private String mTimezone;

    private DateTime mInitialDateTime;

    private DateTime mCountdownDateTime;

    private CountdownCreator() {
        mCountdownDateTime = new DateTime();
        mInitialDateTime = new DateTime();
    }

    // TODO: 6/30/17 Refactor into something other than a builder
    public static CountdownCreator newBuilder() {
        return new CountdownCreator();
    }

    public CountdownCreator setYear(int year) {
        mCountdownDateTime = mCountdownDateTime.withYear(year);
        return this;
    }

    /**
     * January -> 1
     * February -> 2
     */
    public CountdownCreator setMonth(int month) {
        mCountdownDateTime = mCountdownDateTime.withMonthOfYear(month);
        return this;
    }

    /**
     * 1 -> 1
     */
    public CountdownCreator setDayOfMonth(int day) {
        mCountdownDateTime = mCountdownDateTime.withDayOfMonth(day);
        return this;
    }

    /**
     * 10:00 PM -> 22:00 -> 22
     */
    public CountdownCreator setHour(int hour) {
        mCountdownDateTime = mCountdownDateTime.withHourOfDay(hour);
        return this;
    }

    public CountdownCreator setMinute(int minute) {
        mCountdownDateTime = mCountdownDateTime.withMinuteOfHour(minute);
        return this;
    }

    public CountdownCreator setSecond(int second) {
        mCountdownDateTime = mCountdownDateTime.withSecondOfMinute(second);
        return this;
    }

    public CountdownCreator setTitle(String title) {
        mCountdownTitle = title;
        return this;
    }

    public CountdownCreator setDescription(String description) {
        mCountdownDescription = description;
        return this;
    }


    public CountdownCreator setTimezone(TimeZone timezone) {
        mTimezone = timezone.getID();
        return this;
    }

    /**
     * Regenerates the start time for the {@link Countdown} to be built
     */
    public void updateStart() {
        mInitialDateTime = new DateTime();
    }

    /**
     * @return A new {@link Countdown} with the proper title, description, date, time, and timezone
     */
    public Countdown build() {
        return new Countdown(mCountdownTitle, mCountdownDescription, mInitialDateTime.getMillis(),
                mCountdownDateTime.getMillis(), mTimezone);
    }

}
