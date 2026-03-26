package fr.gir.gameedr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.ui.theme.BorderMuted
import fr.gir.gameedr.ui.theme.MistGray
import fr.gir.gameedr.ui.theme.PanelBlack

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionCard(
    title: String,
    prompt: String,
    metadata: List<String>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = prompt,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            metadata.forEach { item ->
                Surface(
                    color = PanelBlack.copy(alpha = 0.66f),
                    border = BorderStroke(1.dp, BorderMuted.copy(alpha = 0.55f)),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MistGray
                    )
                }
            }
        }
    }
}
