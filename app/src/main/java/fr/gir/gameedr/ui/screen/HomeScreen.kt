package fr.gir.gameedr.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.domain.model.GameMode
import fr.gir.gameedr.ui.components.AppScaffold
import fr.gir.gameedr.ui.components.LogoHeader
import fr.gir.gameedr.ui.components.ModeMenuCard
import fr.gir.gameedr.ui.components.SecondaryActionButton
import fr.gir.gameedr.ui.components.SectionCard

@Composable
fun HomeScreen(
    onModeSelected: (GameMode) -> Unit,
    onCredits: () -> Unit
) {
    AppScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            LogoHeader(
                title = "GameEDR",
                size = 200.dp
            )
            SectionCard {
                Text(
                    text = "Centre d'entraînement IPv4",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Choisissez un module tactique. Chaque exercice est indépendant, hors ligne et pensé pour un usage smartphone.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            ModeMenuCard(
                mode = GameMode.NETWORK_CALC,
                summary = "Identifier le réseau et le broadcast à partir d'un hôte IPv4 et d'un masque.",
                onClick = { onModeSelected(GameMode.NETWORK_CALC) }
            )
            ModeMenuCard(
                mode = GameMode.SUBNETTING,
                summary = "Découper un réseau en sous-réseaux égaux, alignés et cohérents.",
                onClick = { onModeSelected(GameMode.SUBNETTING) }
            )
            ModeMenuCard(
                mode = GameMode.VLSM,
                summary = "Allouer les blocs principaux selon les besoins hôtes, le tri et l'alignement.",
                onClick = { onModeSelected(GameMode.VLSM) }
            )
            SecondaryActionButton(
                text = "À propos",
                onClick = onCredits
            )
        }
    }
}
