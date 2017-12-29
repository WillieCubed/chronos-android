package com.craft.apps.countdowns.common.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.craft.apps.countdowns.common.model.Countdown;

/**
 * A {@link ViewModel} that keeps track of a selected {@link Countdown} ID.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class SelectedCountdownViewModel extends ViewModel {

    private MutableLiveData<String> mCountdownId = new MutableLiveData<>();

    /**
     * Returns a mutable {@link android.arch.lifecycle.LiveData} to monitor events.
     *
     * @return A mutable source of data containing a {@link Countdown} ID
     */
    public MutableLiveData<String> getSelectedCountdown() {
        return mCountdownId;
    }

    /**
     * Updates the currently selected countdown.
     *
     * @param countdownId The {@link Countdown} ID to mark as selected
     */
    public void setSelectedCountdown(String countdownId) {
        mCountdownId.setValue(countdownId);
    }
}
