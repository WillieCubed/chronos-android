package com.craft.apps.countdowns.common.database;

import com.google.android.gms.tasks.Task;

/**
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (6/30/17)
 */
public interface Database<T> {

    Task upload(T object);
}
