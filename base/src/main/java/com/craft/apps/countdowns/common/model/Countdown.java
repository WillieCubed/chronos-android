package com.craft.apps.countdowns.common.model;

import com.google.firebase.database.Exclude;
import org.joda.time.DateTime;

/**
 * A POJO used for serializing and deserializing basic countdown information (but not state)
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (3/18/17)
 */
@SuppressWarnings("JavaDoc")
public class Countdown {

    private String title;

    /**
     * A small, one-line (100ish character) description creator a countdown event
     */
    private String description;

    /**
     * The end UNIX time in milliseconds we use to calculate the countdown
     */
    private long finishTime;

    /**
     * The start UNIX time in milliseconds we use to display a user-readable start date and to
     * calculate how long it is
     */
    private long startTime;

    private String timezone;

    /**
     * Required empty public constructor for (de)serialization
     */
    public Countdown() {
    }

    public Countdown(String title, String description, long startTime, long finishTime) {
        this(title, description, finishTime, startTime, null);
    }

    public Countdown(String title, String description, long startTime, long finishTime,
            String timezone) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.timezone = timezone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Exclude
    public DateTime toDateTime() {
        return new DateTime(finishTime);
    }

    @Override
    @Exclude
    public String toString() {
        return "Countdown" +
                " starting at " + getStartTime() +
                " ending at " + getFinishTime() +
                " with timezone " + getTimezone() +
                " described as " + getDescription();
    }
}
