package com.craft.apps.countdowns.feature.widgets.work

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.craft.apps.countdowns.feature.widgets.data.WidgetCountdownRepository
import com.craft.apps.countdowns.feature.widgets.ui.CountdownListWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * A worker that automatically updates widget data.
 */
@HiltWorker
class WidgetUpdateWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val countdownRepository: WidgetCountdownRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // TODO: Update widgets
        CountdownListWidget(countdownRepository).updateAll(appContext)
        return Result.success()
    }
}