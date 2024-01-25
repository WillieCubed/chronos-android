package com.craft.apps.countdowns.ui

import android.app.PendingIntent
import android.app.SearchManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.craft.apps.countdowns.feature.search.CountdownsSuggestionsProvider
import com.craft.apps.countdowns.feature.widgets.ConfigureWidgetActivity
import com.craft.apps.countdowns.feature.widgets.ui.SingleCountdownWidgetReceiver
import com.craft.apps.countdowns.widget.WidgetExtras
import com.craft.apps.countdowns.widget.WidgetType
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        splitInstallManager.installedModules.forEach { module ->
            Log.i("MainActivity", "Installed dynamic module $module")
        }

        enableEdgeToEdge()
        setContent {
            CountdownsApp(onPinCountdown = ::pinToLauncher)
        }

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(
                    this,
                    CountdownsSuggestionsProvider.AUTHORITY,
                    CountdownsSuggestionsProvider.MODE
                ).saveRecentQuery(query, null)
            }
        }
    }

    // TODO: Probably move this to a separate object that gets injected into HomeViewModel
    private fun pinToLauncher(countdownId: Int) {
        val appWidgetProvider = AppWidgetManager.getInstance(this)
        if (!appWidgetProvider.isRequestPinAppWidgetSupported) {
            Log.d(this.javaClass.name, "App pinning not supported, skipping request")
            return
        }
        val countdownDetailWidgetProvider =
            ComponentName(this, SingleCountdownWidgetReceiver::class.java)
        val successCallback = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, ConfigureWidgetActivity::class.java).apply {
                putExtra(WidgetExtras.WIDGET_TYPE, WidgetType.DETAIL)
                putExtra(WidgetExtras.COUNTDOWN_ID, countdownId)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        // Note that this doesn't notify on failure, so we just set a pending intent to trigger
        // If this resolves.
        appWidgetProvider.requestPinAppWidget(
            countdownDetailWidgetProvider,
            null,
            successCallback,
        )
    }

    // TODO: Expose to user in settings
    private fun clearSearchSuggestions() {
        SearchRecentSuggestions(
            this, CountdownsSuggestionsProvider.AUTHORITY, CountdownsSuggestionsProvider.MODE
        ).clearHistory()
    }
}
