package fr.gir.gameedr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.GameMode
import fr.gir.gameedr.ui.theme.BorderMuted
import fr.gir.gameedr.ui.theme.Brass
import fr.gir.gameedr.ui.theme.DarkSoil
import fr.gir.gameedr.ui.theme.PanelBlack
import fr.gir.gameedr.ui.theme.SteelBlue

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DarkSoil.copy(alpha = 0.9f),
        contentColor = Color.White,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, BorderMuted.copy(alpha = 0.78f)),
        tonalElevation = 6.dp,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DarkSoil.copy(alpha = 0.96f),
                            PanelBlack.copy(alpha = 0.92f)
                        )
                    )
                )
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                SteelBlue.copy(alpha = 0.2f),
                                Brass.copy(alpha = 0.95f),
                                SteelBlue.copy(alpha = 0.2f)
                            )
                        ),
                        shape = MaterialTheme.shapes.small
                    )
            )
            content()
        }
    }
}

@Composable
fun LevelCard(
    level: DifficultyLevel,
    detail: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Text(
            text = level.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = level.briefing,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        PrimaryActionButton(
            text = "Démarrer",
            onClick = onClick
        )
    }
}

@Composable
fun ModeMenuCard(
    mode: GameMode,
    summary: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ModeGlyph(
                mode = mode,
                modifier = Modifier.size(54.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "MODULE",
                    style = MaterialTheme.typography.labelLarge,
                    color = SteelBlue
                )
                Text(
                    text = mode.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
        PrimaryActionButton(
            text = "Ouvrir le module",
            onClick = onClick
        )
    }
}
