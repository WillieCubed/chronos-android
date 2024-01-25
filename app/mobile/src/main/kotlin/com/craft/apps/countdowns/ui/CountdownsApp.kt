package com.craft.apps.countdowns.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.craft.apps.countdowns.ui.theme.ChronosTheme

/**
 * The root composable for the Countdowns app.
 */
@Composable
fun CountdownsApp(
    startingDestination: String = "home",
    onPinCountdown: (countdownId: Int) -> Unit,
) {
    val navController = rememberNavController()

    ChronosTheme {
        MainNavigation(
            onPinCountdown,
            startingDestination = startingDestination,
            navController = navController,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Countdowns App")
@Composable
fun CountdownsAppPreview() {
    CountdownsApp(onPinCountdown = {})
}

