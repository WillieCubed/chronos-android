package com.craft.apps.countdowns.common.format;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * A utility class that provides an abstracted way of fetching localized date strings.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleDateFormatter {

    /**
     * @return The provided {@link DateTime} expressed as a date using the current locale
     */
    public static String getLocalDate(Context context, long unixEnd) {
        // TODO: 6/29/17 Accept locale instead of context to be device agnostic
        Locale locale = UnitsFormatter.getDeviceLocale(context);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(
                DateTimeFormat.patternForStyle("M-", locale));
        return formatter.print(new DateTime(unixEnd));
    }
}
