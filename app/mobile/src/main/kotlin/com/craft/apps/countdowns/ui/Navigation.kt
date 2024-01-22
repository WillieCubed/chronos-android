package com.craft.apps.countdowns.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.craft.apps.countdowns.feature.details.ui.DetailsRoute
import com.craft.apps.countdowns.feature.home.HomeRoute

@Composable
fun MainNavigation(
    onPinCountdown: (countdownId: Int) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeRoute(
                onCountdownSelected = { id ->
                    navController.navigate("details/$id")
                },
                onPinCountdown = onPinCountdown,
            )
        }

        composable(
            "details/{id}", listOf(navArgument("id") { type = NavType.LongType })
        ) {
            DetailsRoute(onGoBack = {
                navController.popBackStack()
            })
        }
    }
}

object NavArgs {
    const val COUNTDOWN_ID = "countdownId"
}