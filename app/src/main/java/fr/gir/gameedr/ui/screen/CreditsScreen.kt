package fr.gir.gameedr.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.ui.components.AppScaffold
import fr.gir.gameedr.ui.components.LogoHeader
import fr.gir.gameedr.ui.components.SectionCard

@Composable
fun CreditsScreen(
    onBack: () -> Unit
) {
    AppScaffold(
        title = "\u00C0 propos",
        onBack = onBack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            LogoHeader(size = 96.dp)
            SectionCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Jeu cr\u00E9\u00E9 par le cours R\u00E9seaux et T\u00E9l\u00E9communication du Groupement Informatique et R\u00E9seaux 2026 - CNE LEGENDRE",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "ETNC",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
