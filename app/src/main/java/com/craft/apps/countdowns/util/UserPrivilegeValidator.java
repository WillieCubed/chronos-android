package com.craft.apps.countdowns.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

/**
 * @version 1.0.0
 * @since 1.0.0
 */

public class UserPrivilegeValidator {

    private static final String PREF_KEY_DISABLE_ADS = "privilege_disable_ads";

    public static boolean checkAdsDisabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(PREF_KEY_DISABLE_ADS, false);
    }
}
