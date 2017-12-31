package com.craft.apps.countdowns.common.database;

import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.Countdown;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A {@link Continuation} that maps a {@link DocumentSnapshot} to a {@link Countdown}.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
class CountdownDocumentDeserializer implements Continuation<DocumentSnapshot, Countdown> {

    @Override
    public Countdown then(@NonNull Task<DocumentSnapshot> task) throws Exception {
        return task.getResult().toObject(Countdown.class);
    }
}
