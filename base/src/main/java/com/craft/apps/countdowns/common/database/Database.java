package com.craft.apps.countdowns.common.database;

import com.google.android.gms.tasks.Task;

/**
 * @version 1.0.0
 * @since 1.0.0
 * @deprecated Use {@link CountdownRepository}
 */
@Deprecated
public interface Database<T> {

    Task upload(T object);
}
