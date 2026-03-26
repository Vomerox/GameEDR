package fr.gir.gameedr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.domain.model.ValidationResult
import fr.gir.gameedr.ui.theme.SignalGreen
import fr.gir.gameedr.ui.theme.SignalRed

@Composable
fun CorrectionCard(
    result: ValidationResult,
    modifier: Modifier = Modifier
) {
    val accent = if (result.isCorrect) SignalGreen else SignalRed
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.75f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = result.headline,
                style = MaterialTheme.typography.titleLarge,
                color = accent
            )
            Text(
                text = result.explanation,
                style = MaterialTheme.typography.bodyLarge,
                color = accent
            )
            if (!result.solution.isNullOrBlank()) {
                HorizontalDivider(color = accent.copy(alpha = 0.35f))
                Text(
                    text = "Référence tactique",
                    style = MaterialTheme.typography.titleMedium,
                    color = accent
                )
                result.solution
                    .lineSequence()
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .forEach { line ->
                        val separatorIndex = line.indexOf(':')
                        if (separatorIndex > 0) {
                            val label = line.substring(0, separatorIndex).trim()
                            val value = line.substring(separatorIndex + 1).trim()
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "$label :",
                                    modifier = Modifier.weight(0.38f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = value,
                                    modifier = Modifier.weight(0.62f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
            }
        }
    }
}
