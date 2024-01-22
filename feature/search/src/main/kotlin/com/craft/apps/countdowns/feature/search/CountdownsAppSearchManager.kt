package com.craft.apps.countdowns.feature.search

import android.content.Context
import android.os.Build
import androidx.appsearch.annotation.Document
import androidx.appsearch.app.AppSearchBatchResult
import androidx.appsearch.app.AppSearchSchema
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.app.RemoveByDocumentIdRequest
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.localstorage.LocalStorage
import androidx.appsearch.platformstorage.PlatformStorage
import com.craft.apps.countdowns.core.model.Countdown
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class CountdownsAppSearchManager @Inject constructor(
    @ApplicationContext context: Context,
    coroutineScope: CoroutineScope,
) {
    private val isInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private lateinit var appSearchSession: AppSearchSession

    companion object {
        const val DATABASE_NAME = "countdowns-search-db"
    }

    init {
        coroutineScope.launch {
            appSearchSession = if (Build.VERSION.SDK_INT >= 31) {
                PlatformStorage.createSearchSessionAsync(
                    PlatformStorage.SearchContext.Builder(context, DATABASE_NAME).build()
                ).get()
            } else {
                LocalStorage.createSearchSessionAsync(
                    LocalStorage.SearchContext.Builder(context, DATABASE_NAME).build()
                ).get()
            }

            try {
                val setSchemaRequest =
                    SetSchemaRequest.Builder()
                        .addDocumentClasses(CountdownSearchModel::class.java)
                        .build()
                appSearchSession.setSchemaAsync(setSchemaRequest).get()
                isInitialized.value = true
                awaitCancellation()
            } finally {
                appSearchSession.close()
            }
        }
    }

    /**
     * Adds a [CountdownSearchModel] document to the AppSearch database.
     */
    suspend fun addCountdown(countdown: CountdownSearchModel): AppSearchBatchResult<String, Void> {
        awaitInitialization()
        val request = PutDocumentsRequest.Builder().addDocuments(countdown).build()
        return appSearchSession.putAsync(request).get()
    }

    /**
     * Removes a [CountdownSearchModel] document from the AppSearch database.
     */
    suspend fun removeCountdown(
        id: Int,
        namespace: String = "default",
    ): AppSearchBatchResult<String, Void> {
        awaitInitialization()
        val request = RemoveByDocumentIdRequest.Builder(namespace).addIds(id.toString()).build()
        return appSearchSession.removeAsync(request).get()
    }

    /**
     * Removes multiple [CountdownSearchModel] documents from the AppSearch database.
     */
    suspend fun removeCountdowns(
        id: List<Int>,
        namespace: String = "default",
    ): AppSearchBatchResult<String, Void> {
        awaitInitialization()
        val request = RemoveByDocumentIdRequest.Builder(namespace).addIds(id.toString()).build()
        return appSearchSession.removeAsync(request).get()
    }

    /**
     * Awaits [isInitialized] being set to ```true```.
     */
    private suspend fun awaitInitialization() {
        if (!isInitialized.value) {
            isInitialized.first { it }
        }
    }
}

@Document
data class CountdownSearchModel(
    /**
     * A namespace used to distinguish countdowns.
     *
     * Currently "default" for all countdowns.
     */
    @Document.Namespace val namespace: String = "default",

    /**
     * This countdown's unique identifer.
     */
    @Document.Id val id: String,

    /**
     * A score for a countdown in search. This should increase with the importance of the countdown.
     * For now, a countdown with an end time that is closer to the present has a higher importance.
     */
    @Document.Score val score: Int,

    /**
     * The name of this countdown.
     */
    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_NONE) val label: String,

    // TODO: Change to INDEXING_TYPE_RANGE once AndroidX AppSearch implements this (b/203700301)
    /**
     * The expiration date of this countdown.
     */
    @Document.LongProperty(indexingType = AppSearchSchema.LongPropertyConfig.INDEXING_TYPE_NONE) val timestamp: Long,
)

fun Countdown.toSearchModel(namespace: String = "default"): CountdownSearchModel {
    return CountdownSearchModel(
        namespace = namespace,
        id = this.id.toString(),
        label = this.label,
        timestamp = min(this.expiration.toEpochMilliseconds(), 0),
        score = this.importance,
    )
}