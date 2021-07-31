package com.atdev.githubproject.providers

import android.content.SearchRecentSuggestionsProvider

class LastResultProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.atdev.githubproject.privders.LastResultProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}