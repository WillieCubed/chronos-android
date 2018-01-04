package com.craft.apps.countdowns.common.database;

import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

/**
 * A {@link Continuation} that associates a number of {@link Countdown}s with a {@link User}.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class UserCountdownAdder implements Continuation<User, User> {

    private final String[] countdownIds;

    /**
     * Creates a new UserCountdownAdder to associate the given countdowns with a {@link User}
     * in {@link #then(Task)}.
     */
    public UserCountdownAdder(String... countdowns) {
        this.countdownIds = countdowns;
    }

    /**
     * Returns a user associated with the countdownIds passed in the
     * {@link #UserCountdownAdder(String...) constructor}
     *
     * {@inheritDoc}
     */
    @Override
    public User then(@NonNull Task<User> task) throws Exception {
        User user = task.getResult();
        for (String id : countdownIds) {
            user.getCountdowns().put(id, true);
        }
        return user;
    }
}
