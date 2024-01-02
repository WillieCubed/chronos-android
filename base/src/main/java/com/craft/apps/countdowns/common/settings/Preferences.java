package com.craft.apps.countdowns.common.settings;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.craft.apps.countdowns.common.format.UnitsFormatter;
import com.craft.apps.countdowns.common.model.SortOptions;
import com.craft.apps.countdowns.common.model.SortOptions.SortOption;

/**
 * A proxy class to manage {@link android.content.SharedPreferences} easily
 *
 * @author willie
 * @version 1.0.0
 * @since 3/18/17
 */
@SuppressWarnings("JavaDoc")
public class Preferences {

    public static final String PREF_ONBOARDED = "pref_isOnboarded";

    public static final String PREF_LOCAL_CALENDAR_SYNC = "pref_syncSystemCalendar";

    public static final String PREF_COUNTDOWN_SORT_OPTION = "pref_countdownSortOption";

    public static final String PREF_DISPLAY_UNIT_TYPE = "pref_countdownDisplayUnitType";

    private Context mContext;

    private Preferences(Context context) {
        mContext = context;
    }

    public static Preferences getInstance(Context context) {
        return new Preferences(context);
    }

    /**
     * Completely clears the app's {@link SharedPreferences}
     */
    public void resetPreferences() {
        getPreferences(mContext).edit().clear().apply();
    }

    /**
     * Hidden because we should abstract this away and provide more semantically sound ways to
     * access preferences we require
     */
    private SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isOnboarded() {
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(PREF_ONBOARDED, false);
    }

    public boolean isUsingSystemCalendarSync() {
        return getPreferences(mContext).getBoolean(PREF_LOCAL_CALENDAR_SYNC, false);
    }

    public void setOnboardingState(Context context, boolean isOnboarded) {
        getPreferences(context).edit().putBoolean(PREF_ONBOARDED, isOnboarded).apply();
    }

    public int getDefaultSortOption(Context context) {
        // TODO: 5/27/17 Ensure this isn't too ridiculous
        return getPreferences(context).getInt(PREF_COUNTDOWN_SORT_OPTION, SortOptions.DATE_CREATED);
    }

    public String getDefaultUnitType(Context context) {
        return getPreferences(context).getString(PREF_DISPLAY_UNIT_TYPE, UnitsFormatter.DAYS);
    }

    public void setDefaultSortOption(Context context, @SortOption int sortOption) {
        getPreferences(context).edit().putInt(PREF_COUNTDOWN_SORT_OPTION, sortOption).apply();
    }
}
