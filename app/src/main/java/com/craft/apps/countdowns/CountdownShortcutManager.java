package com.craft.apps.countdowns;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.craft.apps.countdowns.common.model.Countdown;

import java.util.ArrayList;
import java.util.List;

import static com.craft.apps.countdowns.common.util.IntentUtils.ACTION_VIEW_COUNTDOWN_DETAILS;

/**
 * A utility class that allows easy access to updating launcher shortcuts for the home screen.
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class CountdownShortcutManager {

    /**
     * Note: this can only be can be called
     *
     * @param context An Android application context
     * @param countdowns A list of countdowns to add to the app's dynamic shortcuts
     */
    @RequiresApi(api = VERSION_CODES.N_MR1)
    public static void updateShortcuts(@NonNull Context context, List<Countdown> countdowns) {
        if (countdowns == null || countdowns.size() == 0) {
            return;
        }
        ShortcutManager shortcutManager = (ShortcutManager)
                context.getSystemService(Context.SHORTCUT_SERVICE);
        List<ShortcutInfo> shortcuts = new ArrayList<>();
        for (Countdown countdown : countdowns) {
            ShortcutInfo shortcut = new ShortcutInfo.Builder(context, "")
                    .setShortLabel(countdown.getTitle())
                    .setIntent(new Intent(ACTION_VIEW_COUNTDOWN_DETAILS))
                    .build();
            shortcuts.add(shortcut);
        }
        shortcutManager.removeAllDynamicShortcuts();
        shortcutManager.setDynamicShortcuts(shortcuts);
    }
}
