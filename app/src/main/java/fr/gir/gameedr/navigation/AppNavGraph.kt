package fr.gir.gameedr.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.GameMode
import fr.gir.gameedr.ui.screen.CreditsScreen
import fr.gir.gameedr.ui.screen.HomeScreen
import fr.gir.gameedr.ui.screen.LevelSelectionScreen
import fr.gir.gameedr.ui.screen.game.NetworkCalcScreen
import fr.gir.gameedr.ui.screen.game.SubnettingScreen
import fr.gir.gameedr.ui.screen.game.VlsmScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route
    ) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                onModeSelected = { mode ->
                    navController.navigate(AppDestination.Levels.createRoute(mode))
                },
                onCredits = {
                    navController.navigate(AppDestination.Credits.route)
                }
            )
        }

        composable(AppDestination.Credits.route) {
            CreditsScreen(
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = AppDestination.Levels.route,
            arguments = listOf(navArgument(AppDestination.Levels.MODE_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val mode = GameMode.fromRouteSegment(
                backStackEntry.arguments?.getString(AppDestination.Levels.MODE_ARG).orEmpty()
            )

            LevelSelectionScreen(
                mode = mode,
                onBack = {
                    navController.navigateUp()
                },
                onLevelSelected = { level ->
                    navController.navigate(AppDestination.Game.createRoute(mode, level))
                }
            )
        }

        composable(
            route = AppDestination.Game.route,
            arguments = listOf(
                navArgument(AppDestination.Game.MODE_ARG) { type = NavType.StringType },
                navArgument(AppDestination.Game.LEVEL_ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mode = GameMode.fromRouteSegment(
                backStackEntry.arguments?.getString(AppDestination.Game.MODE_ARG).orEmpty()
            )
            val level = DifficultyLevel.fromValue(
                backStackEntry.arguments?.getInt(AppDestination.Game.LEVEL_ARG) ?: 1
            )

            when (mode) {
                GameMode.NETWORK_CALC -> NetworkCalcScreen(
                    level = level,
                    onBack = { navController.navigateUp() },
                    onHome = { navController.popBackStack(AppDestination.Home.route, false) }
                )

                GameMode.SUBNETTING -> SubnettingScreen(
                    level = level,
                    onBack = { navController.navigateUp() },
                    onHome = { navController.popBackStack(AppDestination.Home.route, false) }
                )

                GameMode.VLSM -> VlsmScreen(
                    level = level,
                    onBack = { navController.navigateUp() },
                    onHome = { navController.popBackStack(AppDestination.Home.route, false) }
                )
            }
        }
    }
}
