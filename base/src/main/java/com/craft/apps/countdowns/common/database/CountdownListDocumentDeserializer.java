package com.craft.apps.countdowns.common.database;

import android.support.annotation.NonNull;

import com.craft.apps.countdowns.common.model.Countdown;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * A {@link Continuation} that maps a {@link DocumentSnapshot} to a {@link List<Countdown>}.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
class CountdownListDocumentDeserializer implements
        Continuation<QuerySnapshot, List<Countdown>> {

    @Override
    public List<Countdown> then(@NonNull Task<QuerySnapshot> task) throws Exception {
        return task.getResult().toObjects(Countdown.class);
    }
}
