package com.craft.apps.countdowns.feature.widgets.ui

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalGlanceId
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.feature.widgets.data.CountdownsInfo
import com.craft.apps.countdowns.feature.widgets.data.WidgetCountdownRepository

/**
 * A home screen widget that displays a list of upcoming countdowns.
 */
class CountdownListWidget(
    private val widgetCountdownRepository: WidgetCountdownRepository,
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val manager = GlanceAppWidgetManager(context)
        manager.getAppWidgetId(id)
        val glanceIds = manager.getGlanceIds(this.javaClass)
        provideContent {
            val glanceId = LocalGlanceId.current
            val collected by widgetCountdownRepository.currentCountdowns.collectAsState(
                CountdownsInfo.Loading
            )
            CountdownsWidgetTheme {
                when (collected) {
                    CountdownsInfo.Loading -> {

                    }

                    is CountdownsInfo.Available -> {
//                        val data = (collected as CountdownsInfo.Available)
//                        val countdowns = data.countdowns.filter {
//                            it.widgetId == id
//                        }
                        CountdownListWidgetContent(listOf())
                    }

                    is CountdownsInfo.Unavailable -> {

                    }
                }
            }
        }
    }

    companion object {
        private val thinMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 200.dp)
        private val largeMode = DpSize(260.dp, 280.dp)
    }

    // Define the supported sizes for this widget.
    // The system will decide which one fits better based on the available space
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(thinMode, smallMode, mediumMode, largeMode),
    )
}

@Composable
internal fun CountdownListWidgetContent(
    countdowns: List<Countdown>
) {
    LazyColumn(GlanceModifier.fillMaxSize().background(GlanceTheme.colors.background)) {
        item {
            Row(
                GlanceModifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        }
        items(countdowns) {
            CountdownWidgetListItem(it)
        }
    }
}

@Composable
internal fun CountdownWidgetListItem(countdown: Countdown) {
    Row(
        GlanceModifier.clickable {
//            actionStartActivity<Main>()
        }
    ) {
        Text(countdown.label)
    }
}


fun GlanceModifier.appWidgetBackgroundCornerRadius(): GlanceModifier {
    if (Build.VERSION.SDK_INT >= 31) {
        cornerRadius(android.R.dimen.system_app_widget_background_radius)
    } else {
        cornerRadius(16.dp)
    }
    return this
}

@Composable
fun appWidgetBackgroundModifier() = GlanceModifier
    .fillMaxSize()
    .padding(16.dp)
    .appWidgetBackground()
    .background(GlanceTheme.colors.background)
    .appWidgetBackgroundCornerRadius()

/**
 * Provide a Box composable using the system parameters for app widgets background with rounded
 * corners and background color.
 */
@Composable
fun AppWidgetBox(
    modifier: GlanceModifier = GlanceModifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = appWidgetBackgroundModifier().then(modifier),
        contentAlignment = contentAlignment,
        content = content,
    )
}
