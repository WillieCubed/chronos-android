package com.craft.apps.countdowns.core.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.craft.apps.countdowns.core.theme.LogDateTheme

/**
 * The root composable for the Countdowns app.
 *
 * This controls app-wide navigation
 */
@Composable
fun CountdownsApp() {
    val navController = rememberNavController();
    LogDateTheme {
        HomeScreen()
    }
}