package com.craft.apps.countdowns.common.database;

import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A {@link Continuation} that maps a {@link DocumentSnapshot} to a {@link User}.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public class UserDocumentDeserializer implements Continuation<DocumentSnapshot, User> {

    @Override
    public User then(@NonNull Task<DocumentSnapshot> task) throws Exception {
        return task.getResult().toObject(User.class);
    }
}
