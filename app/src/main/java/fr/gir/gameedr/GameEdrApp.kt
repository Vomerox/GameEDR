package fr.gir.gameedr

import androidx.compose.runtime.Composable
import fr.gir.gameedr.navigation.AppNavGraph
import fr.gir.gameedr.ui.theme.GameEDRTheme

@Composable
fun GameEdrApp() {
    GameEDRTheme {
        AppNavGraph()
    }
}
