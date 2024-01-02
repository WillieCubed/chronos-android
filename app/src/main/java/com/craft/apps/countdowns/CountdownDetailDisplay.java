package com.craft.apps.countdowns;

import androidx.fragment.app.FragmentManager;

/**
 * @author willie
 * @version 1.0.0
 * @since 6/25/17
 */
public interface CountdownDetailDisplay {

    void showDisplay(FragmentManager fragmentManager);

    void collapseDisplay();

    void dismissDisplay();

    void toggleDisplay();

    String getCountdownId();

    void setCountdownId(String countdownId);
}
