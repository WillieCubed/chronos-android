package com.craft.apps.countdowns.feature.wear.complications

import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import com.craft.apps.countdowns.core.data.repository.CountdownRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

/**
 * A complication data source that uses [Countdown]
 */
@AndroidEntryPoint
class CountdownsComplicationService @Inject constructor(
    private val countdownsRepository: CountdownRepository,
) : SuspendingComplicationDataSourceService() {

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        if (type != ComplicationType.SHORT_TEXT) {
            return null
        }
        return createComplicationData("2", "2 days")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {
        // TODO: Fetch ID of complication from storage, load relevant countdown from repository, then display
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> createComplicationData("Sun", "Sunday")
            Calendar.MONDAY -> createComplicationData("Mon", "Monday")
            Calendar.TUESDAY -> createComplicationData("Tue", "Tuesday")
            Calendar.WEDNESDAY -> createComplicationData("Wed", "Wednesday")
            Calendar.THURSDAY -> createComplicationData("Thu", "Thursday")
            Calendar.FRIDAY -> createComplicationData("Fri!", "Friday!")
            Calendar.SATURDAY -> createComplicationData("Sat", "Saturday")
            else -> throw IllegalArgumentException("too many days")
        }
    }

    private fun createComplicationData(text: String, contentDescription: String) =
        ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text).build(),
            contentDescription = PlainComplicationText.Builder(contentDescription).build()
        ).build()
}