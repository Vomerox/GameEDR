package fr.gir.gameedr.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.GameMode
import fr.gir.gameedr.ui.components.AppScaffold
import fr.gir.gameedr.ui.components.LevelCard
import fr.gir.gameedr.ui.components.LogoHeader
import fr.gir.gameedr.ui.components.QuestionCard

@Composable
fun LevelSelectionScreen(
    mode: GameMode,
    onBack: () -> Unit,
    onLevelSelected: (DifficultyLevel) -> Unit
) {
    AppScaffold(
        title = "Choix du niveau",
        subtitle = mode.title,
        onBack = onBack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LogoHeader(size = 108.dp)
            QuestionCard(
                title = "Cadre d'exercice",
                prompt = mode.description,
                metadata = listOf("4 niveaux", "Progression guidée", "Correction immédiate")
            )
            DifficultyLevel.entries.forEach { level ->
                LevelCard(
                    level = level,
                    detail = levelDetail(mode, level),
                    onClick = { onLevelSelected(level) }
                )
            }
        }
    }
}

private fun levelDetail(mode: GameMode, level: DifficultyLevel): String {
    return when (mode) {
        GameMode.NETWORK_CALC -> when (level) {
            DifficultyLevel.LEVEL_1 -> "Bases : /24 à /27 et blocs réguliers."
            DifficultyLevel.LEVEL_2 -> "Intermédiaire : /23 à /28 et masques variés."
            DifficultyLevel.LEVEL_3 -> "Avancé : réseaux plus larges et pas moins évidents."
            DifficultyLevel.LEVEL_4 -> "Opérationnel : CIDR large, masques fins et cas rapides."
        }

        GameMode.SUBNETTING -> when (level) {
            DifficultyLevel.LEVEL_1 -> "Découpage simple en 2 ou 4 sous-réseaux."
            DifficultyLevel.LEVEL_2 -> "Masques plus variés et jusqu'à 8 sous-réseaux."
            DifficultyLevel.LEVEL_3 -> "Réseaux de départ plus larges, alignements à contrôler."
            DifficultyLevel.LEVEL_4 -> "Découpage dense avec contrôle strict des incréments."
        }

        GameMode.VLSM -> when (level) {
            DifficultyLevel.LEVEL_1 -> "Deux blocs principaux et tri facile."
            DifficultyLevel.LEVEL_2 -> "Trois blocs et choix de masque plus serré."
            DifficultyLevel.LEVEL_3 -> "Quatre blocs avec impact du tri sur toute l'allocation."
            DifficultyLevel.LEVEL_4 -> "Plan VLSM complet avec besoins plus hétérogènes."
        }
    }
}
