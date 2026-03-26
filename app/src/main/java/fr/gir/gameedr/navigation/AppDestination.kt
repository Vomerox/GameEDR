package fr.gir.gameedr.navigation

import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.GameMode

sealed interface AppDestination {
    val route: String

    data object Home : AppDestination {
        override val route: String = "home"
    }

    data object Credits : AppDestination {
        override val route: String = "credits"
    }

    data object Levels : AppDestination {
        const val MODE_ARG = "mode"
        override val route: String = "levels/{$MODE_ARG}"

        fun createRoute(mode: GameMode): String {
            return "levels/${mode.routeSegment}"
        }
    }

    data object Game : AppDestination {
        const val MODE_ARG = "mode"
        const val LEVEL_ARG = "level"
        override val route: String = "game/{$MODE_ARG}/{$LEVEL_ARG}"

        fun createRoute(mode: GameMode, level: DifficultyLevel): String {
            return "game/${mode.routeSegment}/${level.value}"
        }
    }
}
