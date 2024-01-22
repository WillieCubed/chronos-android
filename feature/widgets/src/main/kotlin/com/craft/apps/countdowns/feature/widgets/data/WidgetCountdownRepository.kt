package com.craft.apps.countdowns.feature.widgets.data

import com.craft.apps.countdowns.core.coroutines.IoDispatcher
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import com.craft.apps.countdowns.core.database.entity.toDataModel
import com.craft.apps.countdowns.core.database.entity.LocalWidgetDataModel
import com.craft.apps.countdowns.core.database.entity.WidgetDataAndCountdownModel
import com.craft.apps.countdowns.core.database.dao.WidgetDataDao
import com.craft.apps.countdowns.core.model.Countdown
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus
import javax.inject.Inject

/**
 * A repository that keeps track of data for home screen widgets.
 */
class WidgetCountdownRepository @Inject constructor(
    private val widgetMappingDataSource: WidgetMappingDataSource,
    private val countdownRepository: CountdownRepository,
) {

    private var lastRun: Instant = Instant.DISTANT_PAST
    private val mutex = Mutex()
    private val _currentCountdowns = MutableStateFlow<CountdownsInfo>(CountdownsInfo.Loading)

    val currentCountdowns: StateFlow<CountdownsInfo> = _currentCountdowns

    suspend fun addCountdownWidget(countdownId: Int, widgetId: String) {
        val countdown = countdownRepository[countdownId].first()
        // TODO: Handle case where given countdown doesn't exist
        widgetMappingDataSource.addWidget(WidgetData(widgetId, countdown))
    }

    fun getById(widgetId: String) = _currentCountdowns.map {
        if (it is CountdownsInfo.Available) {
            return@map it
        } else {

        }
    }

    suspend fun updateData() {
        mutex.withLock(lastRun) {
            if (lastRun.plus(30, DateTimeUnit.SECOND) > Clock.System.now()) {
                return
            }
            lastRun = Clock.System.now()
        }
        _currentCountdowns.value = CountdownsInfo.Loading
    }
}

sealed interface CountdownsInfo {
    data object Loading : CountdownsInfo
    data class Available(
        val countdowns: List<WidgetData>
    ) : CountdownsInfo

    data class Unavailable(val throwable: Throwable) : CountdownsInfo
}

data class WidgetData(
    val widgetId: String,
    val countdown: Countdown,
)

class WidgetMappingDataSource @Inject constructor(
    private val widgetDataDao: WidgetDataDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    val data = widgetDataDao.queryWidgetData().map {
        it.map { data -> data.toRepositoryModel() }
    }

    suspend fun addWidget(widgetData: WidgetData) = withContext(ioDispatcher) {
        widgetDataDao.insertWidgetData(widgetData.toLocalModel())
    }
}

/**
 * Utility function to convert a database-level widget data model to a repository-level one.
 */
fun WidgetDataAndCountdownModel.toRepositoryModel() = WidgetData(
    widgetId = this.localWidgetDataModel.widgetId,
    countdown = this.countdown.toDataModel(),
)

/**
 * Utility function to convert a repository-level widget model to a database-level one.
 */
fun WidgetData.toLocalModel() = LocalWidgetDataModel(
    countdownId = this.countdown.id,
    widgetId = this.widgetId,
)