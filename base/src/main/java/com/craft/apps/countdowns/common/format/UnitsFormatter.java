package com.craft.apps.countdowns.common.format;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.core.content.res.ResourcesCompat;
import androidx.palette.graphics.Palette;
import android.widget.ImageView;
import android.widget.TextView;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.model.Countdown;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A utility class that formats data from a {@link Countdown} into a proper display format
 */
public class UnitsFormatter {

    public static final String SECONDS = "seconds";
    public static final String MINUTES = "minutes";
    public static final String HOURS = "hours";
    public static final String DAYS = "days";
    public static final String WEEKS = "weeks";
    public static final String MONTHS = "months";
    public static final String YEARS = "years";
    public static final String DECADES = "decades";
    public static final String CENTURIES = "centuries";
    public static final String MILLENNIA = "millennia";
    public static final String DISPLAY_AUTO = "auto";
    /**
     * An integer representing the highest urgency level for a {@link Countdown}
     */
    public static final int URGENCY_LEVEL_1 = 1;
    /**
     * An integer representing the second to highest urgency level for a {@link Countdown}
     */
    public static final int URGENCY_LEVEL_2 = 2;
    /**
     * An integer representing the third to highest urgency level for a {@link Countdown}
     */
    public static final int URGENCY_LEVEL_3 = 3;
    /**
     * An integer representing the third to lowest urgency level for a {@link Countdown}
     */
    public static final int URGENCY_LEVEL_4 = 4;
    /**
     * An integer representing the second to lowest urgency level for a {@link Countdown}
     */
    public static final int URGENCY_LEVEL_5 = 5;
    /**
     * An integer representing the lowest urgency level for a {@link Countdown}
     */
    public static final int URGENCY_LEVEL_6 = 6;
    private static final String TAG = UnitsFormatter.class.getSimpleName();
    private static final int UNSET = -1;
    private static final double URGENCY_LEVEL_1_THRESHOLD = 0.9;
    private static final double URGENCY_LEVEL_2_THRESHOLD = 0.75;
    private static final double URGENCY_LEVEL_3_THRESHOLD = 0.5;
    private static final double URGENCY_LEVEL_4_THRESHOLD = 0.4;
    private static final double URGENCY_LEVEL_5_THRESHOLD = 0.2;
    private static final double URGENCY_LEVEL_6_THRESHOLD = 0.1;
    private static final int AMOUNT_THOUSAND = (int) Math.pow(10, 3);
    private static final int AMOUNT_MILLION = (int) Math.pow(10, 6);
    private static final int AMOUNT_BILLION = (int) Math.pow(10, 9);
    private static final int AMOUNT_TRILLION = (int) Math.pow(10, 9);
    private Context context;
    private long startTime = UNSET;
    private long endTime = UNSET;
    private boolean useEndDate = false;
    @UnitType
    private String unitType;

    public UnitsFormatter(Context context) {
        this.context = context;
    }

    private UnitsFormatter(long endTime) {
        this.endTime = endTime;
    }

    private UnitsFormatter(long time, boolean useStart) {
        if (useStart) {
            this.startTime = time;
        } else {
            this.endTime = time;
        }
    }

    public static UnitsFormatter withEnd(long time) {
        return new UnitsFormatter(time);
    }

    public static UnitsFormatter withStart(long time) {
        return new UnitsFormatter(time, true);
    }

//    ================================== Screw this, I'll figure it out later =====================
//    ================================== Screw this, I'll figure it out later =====================
//    ================================== Screw this, I'll figure it out later =====================

    @NonNull
    public static UnitsFormatter creator(Context context) {
        return new UnitsFormatter(context);
    }

    /**
     * Provides the number creator hours/days/weeks/months/years until a given end time
     *
     * @param endUnixTime The end time represented by a UNIX timestamp
     * @param unitType A supported unit type included in the {@link UnitType} list
     * @return The number creator date units until the specified date
     */
    public static int getUnitsUntil(long endUnixTime, @UnitType String unitType) {
        DateTime currentDate = new DateTime();
        DateTime finishDate = new DateTime(endUnixTime);
        int result;
        switch (unitType) {
            case HOURS:
                result = Hours.hoursBetween(currentDate, finishDate).getHours();
                break;
            default:
            case DAYS:
                result = Days.daysBetween(currentDate, finishDate).getDays();
                break;
            case WEEKS:
                result = Weeks.weeksBetween(currentDate, finishDate).getWeeks();
                break;
            case MONTHS:
                result = Months.monthsBetween(currentDate, finishDate).getMonths();
                break;
            case YEARS:
                result = Years.yearsBetween(currentDate, finishDate).getYears();
                break;
        }
        return result;
    }

    @UrgencyLevel
    public static int getUrgencyLevel(long startUnixTime, long endUnixTime,
            @UnitType String unitType) {
        DateTime endTime = new DateTime(endUnixTime);
        double determinedUrgency;
        switch (unitType) {
            default:
            case HOURS:
            case DAYS:
            case WEEKS:
            case MONTHS:
            case YEARS:
                Days daysTotal = Duration.millis(endUnixTime - startUnixTime).toStandardDays();
                Days daysUntilEnd = Duration.millis(endUnixTime - DateTime.now().getMillis())
                        .toStandardDays();
                if (daysUntilEnd.getDays() == 0) {
                    determinedUrgency = 1;
                } else {
                    determinedUrgency = daysTotal.getDays() / daysUntilEnd.getDays();
                }
        }
        if (determinedUrgency > URGENCY_LEVEL_6_THRESHOLD) {
            return URGENCY_LEVEL_6;
        } else if (determinedUrgency < URGENCY_LEVEL_5_THRESHOLD) {
            return URGENCY_LEVEL_5;
        } else if (determinedUrgency < URGENCY_LEVEL_4_THRESHOLD) {
            return URGENCY_LEVEL_4;
        } else if (determinedUrgency < URGENCY_LEVEL_3_THRESHOLD) {
            return URGENCY_LEVEL_3;
        } else if (determinedUrgency < URGENCY_LEVEL_2_THRESHOLD) {
            return URGENCY_LEVEL_2;
        } else {
            return URGENCY_LEVEL_1;
        }
    }

    public static String getDate(long unixTime) {
        // TODO: 3/16/17 Add localization support and whatnot
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMM d, yyyy");
        return formatter.print(new DateTime(unixTime));
    }

    public static String getFullCountdownString(int unitAmount,
            @UnitType String formatOption) {
        String result = String.valueOf(unitAmount);
        switch (formatOption) {
            case HOURS:
                result += HOURS;
                break;
            default:
            case DAYS:
                break;
            case WEEKS:
                break;
            case MONTHS:
                break;
            case YEARS:
                break;
        }
        return result;
    }

    public static String getSimpleAmount(int unitAmount) {
        if (unitAmount <= 0) {
            return String.valueOf(0);
        }
        String result = String.valueOf(unitAmount);
        String modifier = "";
        StringBuilder builder = new StringBuilder(result);
        if (unitAmount >= AMOUNT_TRILLION) {
            modifier = "T";
        } else if (unitAmount >= AMOUNT_BILLION) {
            modifier = "B";
        } else if (unitAmount >= AMOUNT_MILLION) {
            modifier = "M";
        } else if (unitAmount >= AMOUNT_THOUSAND) {
            modifier = "K";
        }
        if (!modifier.isEmpty()) {
            builder.insert(1, ".")
                    .delete(2, result.length());
        }
        return builder.append(modifier)
                .toString();
    }

    public static void colorCountdownBackground(final ImageView background,
            @UrgencyLevel int urgencyLevel) {
        Context context = background.getContext();
        int backgroundColor;
        switch (urgencyLevel) {
            case URGENCY_LEVEL_1:
                backgroundColor = ResourcesCompat.getColor(context.getResources(),
                        R.color.color_urgency_level_1, context.getTheme());

            case URGENCY_LEVEL_2:
                backgroundColor = ResourcesCompat.getColor(context.getResources(),
                        R.color.color_urgency_level_2, context.getTheme());

            case URGENCY_LEVEL_3:
                backgroundColor = ResourcesCompat.getColor(context.getResources(),
                        R.color.color_urgency_level_3, context.getTheme());

            case URGENCY_LEVEL_4:
                backgroundColor = ResourcesCompat.getColor(context.getResources(),
                        R.color.color_urgency_level_4, context.getTheme());

            case URGENCY_LEVEL_5:
                backgroundColor = ResourcesCompat.getColor(context.getResources(),
                        R.color.color_urgency_level_5, context.getTheme());

            default:
            case URGENCY_LEVEL_6:
                backgroundColor = ResourcesCompat.getColor(context.getResources(),
                        R.color.color_urgency_level_6, context.getTheme());
        }
        background.setImageDrawable(new ColorDrawable(backgroundColor));
    }

    public static void colorTextView(final TextView textView, ImageView background,
            @UrgencyLevel int urgencyLevel) {
        Bitmap bitmap = background.getDrawingCache();
        Palette.from(bitmap).generate(palette -> {
            int textColor = palette.getVibrantSwatch().getTitleTextColor();
            textView.setTextColor(textColor);
        });
    }

    @TargetApi(VERSION_CODES.N)
    public static Locale getDeviceLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }

    public UnitsFormatter startingAt(long time) {
        this.startTime = time;
        return this;
    }

    public UnitsFormatter endingAt(long time) {
        this.endTime = time;
        return this;
    }

    public UnitsFormatter startingNow() {
        return startingAt(DateTime.now().getMillis());
    }

    public UnitsFormatter endingNow() {
        return endingAt(DateTime.now().getMillis());
    }

    public UnitsFormatter usingType(@UnitType String type) {
        this.unitType = type;
        return this;
    }

    public UnitsFormatter usingStart() {
        this.useEndDate = false;
        return this;
    }

    public String as(@UnitType String type) {
        Period period = new Period(this.startTime, this.endTime);
        long time = period.getMillis();
        switch (type) {
            case MILLENNIA:
                return String.valueOf(Years.years((int) time).getYears() / 1_000);
            case DECADES:
                return String.valueOf(Years.years((int) time).getYears() / 10);
            case YEARS:
                return String.valueOf(Years.years((int) time).getYears());
            case MONTHS:
                return String.valueOf(Months.months((int) time).getMonths());
            case WEEKS:
                return String.valueOf(Weeks.weeks((int) time).getWeeks());
            case DAYS:
                return String.valueOf(Days.days((int) time).getDays());
            case HOURS:
                return String.valueOf(Hours.hours((int) time).getHours());
            case MINUTES:
                return String.valueOf(Minutes.minutes((int) time).getMinutes());
            case SECONDS:
                return String.valueOf(Seconds.seconds((int) time).getSeconds());
            default:
                throw new IllegalArgumentException("\"" + type + "\" is not a UnitType!");
        }
    }

    public String asDate(Locale locale) {
        DateTime dateTime;
        if (this.startTime == UNSET && this.endTime == UNSET) {
            throw new IllegalArgumentException(
                    "Start time or end time must be set using \"startingAt()\" or \"endingAt()\"");
        } else if (this.startTime != UNSET) {
            dateTime = new DateTime(this.startTime);
        } else {
            dateTime = new DateTime(this.endTime);
        }
//        String pattern = DateTimeFormat.patternForStyle("L" , locale);
        DateTimeFormatter formatter = DateTimeFormat.longDate().withLocale(locale);
        return dateTime.toString(formatter);
    }

    public String automaticallyFormatted() {
        // TODO: 5/31/17 Replace with machine learning and whatnot
        Period period = getDuration().toPeriod();
        if (period.getYears() >= 1_000) {
            return as(MILLENNIA);
        } else if (period.getYears() >= 100) {
            return as(CENTURIES);
        } else if (period.getYears() >= 10) {
            return as(DECADES);
        } else if (period.getMonths() >= 12) {
            return as(YEARS);
        } else if (period.getMonths() >= 1) {
            return as(MONTHS);
        } else if (period.getDays() >= 7) {
            return as(WEEKS);
        } else if (period.getDays() >= 1) {
            return as(DAYS);
        } else if (period.getMinutes() >= 60) {
            return as(HOURS);
        } else if (period.getSeconds() >= 60) {
            return as(MINUTES);
        }
        return as(SECONDS);
    }

    /**
     * Provides a way creator getting versions creator numbers using significant digits
     * Example: returns 1.2K
     */
    public String asNumberLabel() {
        long difference = Long.valueOf(as(this.unitType));
        if (difference <= 0) {
            return String.valueOf(0);
        }
        String result = String.valueOf(difference);
        String modifier = "";
        StringBuilder builder = new StringBuilder(result);
        if (difference >= AMOUNT_TRILLION) {
            modifier = "T";
        } else if (difference >= AMOUNT_BILLION) {
            modifier = "B";
        } else if (difference >= AMOUNT_MILLION) {
            modifier = "M";
        } else if (difference >= AMOUNT_THOUSAND) {
            modifier = "K";
        }
        if (!modifier.isEmpty()) {
            // Get the first two significant figures and kill the rest
            builder.insert(1, ".").delete(2, result.length());
        }
        return builder.append(modifier).toString();
    }

    public long asDifference() {
        if (this.startTime == UNSET) {
            throw new IllegalArgumentException("Start time must be set using \"startingAt()\"");
        }
        if (this.endTime == UNSET) {
            throw new IllegalArgumentException("End time must be set using \"endingAt()\"");
        }
        return getDifference(this.startTime, this.endTime);
    }

    @NonNull
    private Period getPeriod() {
        return new Period(this.endTime, this.startTime);
    }

    private Duration getDuration() {
        return new Duration(this.startTime, this.endTime);
    }

    private long getDifference(long start, long end) {
        return end - start;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SECONDS,
            MINUTES,
            HOURS,
            DAYS,
            WEEKS,
            MONTHS,
            YEARS,
            DECADES,
            CENTURIES,
            MILLENNIA
    })
    public @interface UnitType {

    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            URGENCY_LEVEL_1,
            URGENCY_LEVEL_2,
            URGENCY_LEVEL_3,
            URGENCY_LEVEL_4,
            URGENCY_LEVEL_5,
            URGENCY_LEVEL_6
    })
    public @interface UrgencyLevel {

    }
}
