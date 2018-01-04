package com.craft.apps.countdowns.common.data;

import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.util.Log;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.ClassSnapshotParser;
import com.firebase.ui.firestore.FirestoreArray;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

/**
 * A {@link android.arch.lifecycle.LiveData} that monitors a given {@link Query} and notifies
 * any listeners of data changes.
 *
 * @param <T> The type of object to serialize the data
 * @version 1.0.0
 * @since 2.0.0
 */
public class FirestoreQueryLiveData<T> extends MutableLiveData<T> implements ChangeEventListener {

    private static final String TAG = FirestoreQueryLiveData.class.getSimpleName();
    private final Handler mHandler = new Handler();
    private SnapshotParser<T> mSnapshotParser;
    private FirestoreArray<T> mSnapshots;
    private ChangeEventListener mChangeListener;
    private boolean mListenerRemovePending = false;

    private final Runnable mRemovalListener = () -> {
        mSnapshots.removeChangeEventListener(mChangeListener);
        mListenerRemovePending = false;
    };

    /**
     * @param query A query to watch for data changes
     * @param tClass The class of object to deserialize data
     */
    public FirestoreQueryLiveData(Query query, Class<T> tClass) {
        mSnapshotParser = new ClassSnapshotParser<>(tClass);
        mSnapshots = new FirestoreArray<>(query, mSnapshotParser);
    }

    @Override
    @CallSuper
    protected void onActive() {
        if (mListenerRemovePending) {
            mHandler.removeCallbacks(mRemovalListener);
        } else {
            mChangeListener = mSnapshots.addChangeEventListener(this);
        }
        mListenerRemovePending = false;
    }

    @Override
    @CallSuper
    protected void onInactive() {
        // Listener removal is schedule on a two second delay
        mHandler.postDelayed(mRemovalListener, 2000);
        mListenerRemovePending = true;
    }

    @Override
    @CallSuper
    public void onChildChanged(ChangeEventType type, DocumentSnapshot snapshot, int newIndex, int oldIndex) {
        setValue(mSnapshotParser.parseSnapshot(snapshot));
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.w(TAG, "Error when querying data", e);
    }
}
