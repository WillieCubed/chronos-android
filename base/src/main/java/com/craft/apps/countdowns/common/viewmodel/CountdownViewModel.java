package com.craft.apps.countdowns.common.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.data.FirebaseQueryLiveData;
import com.craft.apps.countdowns.common.data.SingleCountdownDeserializer;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.model.Countdown;

/**
 * @version 1.0.0
 * @since 2.0.0
 */
public class CountdownViewModel extends ViewModel {

    private LiveData<Countdown> mLiveData;

    public void setCountdownSource(String countdownId) {
        mLiveData = Transformations.map(
                new FirebaseQueryLiveData(OldDatabase.getCountdownReference(countdownId)),
                new SingleCountdownDeserializer());
    }

    @NonNull
    public LiveData<Countdown> getCountdown() {
        return mLiveData;
    }
}
