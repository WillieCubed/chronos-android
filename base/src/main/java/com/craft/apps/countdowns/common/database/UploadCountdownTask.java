package com.craft.apps.countdowns.common.database;

import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.Countdown;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

/**
 * @version 1.0.0
 * @since 2.0.0
 */
class UploadCountdownTask implements Continuation<Countdown, Task<Void>> {

    private String userId;

    /**
     * Creates a new UploadCountdownTask that will associate a {@link Countdown} with the given
     * userId
     */
    UploadCountdownTask(String userId) {
        this.userId = userId;
    }

    @Override
    public Task<Void> then(@NonNull Task<Countdown> task) throws Exception {
        Countdown countdown = task.getResult();
        return UserRepository.linkCountdownToUser(userId, countdown);
    }
}
