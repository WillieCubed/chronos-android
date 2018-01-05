package com.craft.apps.countdowns.widget;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.ClassSnapshotParser;
import com.firebase.ui.firestore.FirestoreArray;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import static android.widget.RemoteViewsService.RemoteViewsFactory;

/**
 * An adapter for a Cloud Firestore-backed {@link RemoteViewsFactory}.
 *
 * @param <T> The type of object to deserialize data
 * @version 1.0.0
 * @since 2.0.0
 */
@SuppressWarnings("WeakerAccess")
public abstract class FirestoreListRemoteViewsFactory<T> implements RemoteViewsFactory,
        ChangeEventListener {

    private static final String TAG = FirestoreListRemoteViewsFactory.class.getSimpleName();

    private Context mContext;

    private FirestoreArray<T> mSnapshots;

    private ChangeEventListener mChangeListener;

    /**
     * Creates a new FirestoreListRemoteViewsFactory monitoring the given query.
     *
     * @param context An context from which an application context will be extracted
     */
    public FirestoreListRemoteViewsFactory(Query query, @NonNull Context context, Class<T> tClass) {
        tClass = Objects.requireNonNull(tClass, "Class must not be null");
        mContext = context.getApplicationContext(); // This is for a widget
        mSnapshots = new FirestoreArray<>(query, new ClassSnapshotParser<>(tClass));
    }

    /**
     * Returns the model item at the given position.
     */
    public T getItem(int position) {
        return mSnapshots.get(position);
    }

    /**
     * Returns the {@link DocumentSnapshot} at the given position.
     */
    public DocumentSnapshot getSnapshot(int position) {
        return mSnapshots.getSnapshot(position);
    }

    /**
     * Returns the application context passed into the constructor.
     */
    @NonNull
    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        if (mChangeListener == null) {
            mChangeListener = mSnapshots.addChangeEventListener(this);
        }
    }

    @Override
    public void onDataSetChanged() {
        // For overriding
    }

    @Override
    public void onDestroy() {
        if (mChangeListener != null) {
            mSnapshots.removeChangeEventListener(mChangeListener);
            mChangeListener = null;
        }
    }

    @Override
    public int getCount() {
        return mSnapshots.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mSnapshots.getSnapshot(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    @CallSuper
    public void onChildChanged(ChangeEventType type, DocumentSnapshot snapshot, int newIndex,
                               int oldIndex) {
        onDataSetChanged();
    }

    @Override
    @CallSuper
    public void onDataChanged() {
        // For overriding
        onDataSetChanged();
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.w(TAG, "Error when updating data", e);
    }
}
