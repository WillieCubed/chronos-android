package com.craft.apps.countdowns.search

import android.content.SearchRecentSuggestionsProvider

class CountdownsSuggestionsProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.craft.apps.countdowns.search.CountdownsSuggestionsProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}