package com.craft.apps.countdowns.common.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.data.CountdownListDeserializer;
import com.craft.apps.countdowns.common.data.FirebaseQueryLiveData;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.model.Countdown;

import java.util.List;

/**
 * @version 1.0.0
 * @since 2.0.0
 */
public class CountdownListViewModel extends ViewModel {

    private LiveData<List<Countdown>> mLiveData;

    public void setCountdownSource(String userId) {
        mLiveData = Transformations.map(
                new FirebaseQueryLiveData(OldDatabase.getUserCountdownsReference(userId)),
                new CountdownListDeserializer());
    }

    @NonNull
    public LiveData<List<Countdown>> getCountdowns() {
        return mLiveData;
    }

    public void updateCountdown(String id, Countdown countdown) {

    }
}
