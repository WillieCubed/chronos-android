package com.craft.apps.countdowns.wear.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.craft.apps.countdowns.feature.wear.home.ui.HomeRoute

@Composable
fun MainNavigation(
    navController: NavHostController = rememberSwipeDismissableNavController()
) {

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeRoute()
        }
    }
}
