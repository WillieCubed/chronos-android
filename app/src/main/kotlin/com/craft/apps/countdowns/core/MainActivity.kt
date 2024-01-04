package com.craft.apps.countdowns.core

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.craft.apps.countdowns.core.ui.CountdownsApp
import com.craft.apps.countdowns.search.CountdownsSuggestionsProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        enableEdgeToEdge()
        setContent {
            CountdownsApp()
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

    // TODO: Expose to user in settings
    private fun clearSearchSuggestions() {
        SearchRecentSuggestions(
            this, CountdownsSuggestionsProvider.AUTHORITY, CountdownsSuggestionsProvider.MODE
        ).clearHistory()
    }
}

