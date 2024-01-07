package com.craft.apps.countdowns.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.craft.apps.countdowns.core.ui.details.DetailScreen
import com.craft.apps.countdowns.core.ui.home.HomeScreen
import com.craft.apps.countdowns.theme.CountdownsTheme

/**
 * The root composable for the Countdowns app.
 *
 * This controls app-wide navigation.
 */
@Composable
fun CountdownsApp(
    onPinCountdown: (countdownId: Int) -> Unit,
) {
    val navController = rememberNavController()

    CountdownsTheme {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    onCountdownSelected = { countdownId ->
                        navController.navigate("countdown/${countdownId}")
                    },
                    onPinCountdown = onPinCountdown,
                )
            }
            composable(
                "countdown/{${NavArgs.COUNTDOWN_ID}}", arguments = listOf(
                    navArgument(NavArgs.COUNTDOWN_ID) {
                        type = NavType.IntType
                    },
                )
            ) {
                val countdownId = requireNotNull(it.arguments).getInt(NavArgs.COUNTDOWN_ID)
                DetailScreen(countdownId = countdownId)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "Countdowns App")
@Composable
fun CountdownsAppPreview() {
    CountdownsApp(onPinCountdown = {})
}

object NavArgs {
    const val COUNTDOWN_ID = "countdownId"
}