package com.example.routing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.routing.ui.form.FormScreen
import com.example.routing.ui.history.HistoryScreen
import com.example.routing.ui.postal.PostalScreen

object Destinations {
    const val FORM = "form"
    const val HISTORY = "history"
    const val POSTAL_ROUTE = "postal/{routeId}"
    const val ARG_ROUTE_ID = "routeId"

    fun postalRoute(routeId: String) = "postal/$routeId"
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.FORM,
        modifier = modifier
    ) {
        composable(route = Destinations.FORM) {
            FormScreen(
                onRouteGenerated = { routeId ->
                    navController.navigate(Destinations.postalRoute(routeId))
                },
                onOpenHistory = {
                    navController.navigate(Destinations.HISTORY)
                }
            )
        }

        composable(route = Destinations.HISTORY) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onOpenRoute = { routeId ->
                    navController.navigate(Destinations.postalRoute(routeId))
                }
            )
        }

        composable(
            route = Destinations.POSTAL_ROUTE,
            arguments = listOf(
                navArgument(Destinations.ARG_ROUTE_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments
                ?.getString(Destinations.ARG_ROUTE_ID)
                .orEmpty()

            PostalScreen(
                routeId = routeId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
