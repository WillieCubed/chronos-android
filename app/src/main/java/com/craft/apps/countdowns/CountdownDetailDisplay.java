package com.craft.apps.countdowns;

import android.support.v4.app.FragmentManager;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CountdownDetailDisplay {

    void showDisplay(FragmentManager fragmentManager);

    void collapseDisplay();

    void dismissDisplay();

    void toggleDisplay();

    String getCountdownId();

    void setCountdownId(String countdownId);
}
