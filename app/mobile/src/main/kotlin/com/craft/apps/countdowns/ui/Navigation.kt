package com.craft.apps.countdowns.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.craft.apps.countdowns.feature.details.DetailsRoute
import com.craft.apps.countdowns.feature.home.HomeRoute

@Composable
fun MainNavigation(
    onPinCountdown: (countdownId: Int) -> Unit,
    startingDestination: String = "home",
    navController: NavHostController = rememberNavController(),
) {
    Log.d("MainNavigation", "Starting at destination $startingDestination")
    NavHost(navController = navController, startDestination = startingDestination) {
        homeScreen(
            onNavigateToCountdown = { id ->
                navController.navigate("details/$id")
            },
            onPinCountdown = onPinCountdown,
        )

        detailsScreen(onGoBack = {
            navController.popBackStack()
        })
    }
}

fun NavGraphBuilder.homeScreen(
    onNavigateToCountdown: (countdownId: Int) -> Unit,
    onPinCountdown: (countdownId: Int) -> Unit,
) {
    composable(
        "home?${NavArgs.CREATE_NEW}={${NavArgs.CREATE_NEW}}",
        arguments = listOf(navArgument(NavArgs.CREATE_NEW) {
            type = NavType.BoolType
            defaultValue = false
        })
    ) {
        Log.d("MainNavigationHost", "Current args: ${it.arguments}")
        val shouldCreateNew = it.arguments?.getBoolean(NavArgs.CREATE_NEW) ?: false
        HomeRoute(
            shouldCreateNewCountdown = shouldCreateNew,
            onCountdownSelected = onNavigateToCountdown,
            onPinCountdown = onPinCountdown,
        )
    }
}

fun NavGraphBuilder.detailsScreen(
    onGoBack: () -> Unit,
) {
    composable(
        "details/{$NavArgs.COUNTDOWN_ID}",
        listOf(navArgument(NavArgs.COUNTDOWN_ID) {
            type = NavType.LongType
        }),
        deepLinks = DEEP_LINKS,
    ) {
        DetailsRoute(
            onGoBack = onGoBack,
        )
    }
}

val DEEP_LINKS = listOf(
    navDeepLink { uriPattern = "https://chronos.williecubed.dev/countdown/{id}" },
    navDeepLink { uriPattern = "chronos://countdown/{id}" },
)

object NavArgs {
    const val COUNTDOWN_ID = "id"
    const val CREATE_NEW = "create_new"
}