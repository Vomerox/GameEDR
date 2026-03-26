package fr.gir.gameedr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.ui.theme.BorderMuted
import fr.gir.gameedr.ui.theme.DarkSoil

@Composable
fun GameActionDock(
    onValidate: () -> Unit,
    onNext: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = DarkSoil.copy(alpha = 0.94f),
        contentColor = Color.White,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, BorderMuted.copy(alpha = 0.7f)),
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Console d'action",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            PrimaryActionButton(
                text = "Valider",
                onClick = onValidate,
                buttonHeight = 48.dp
            )
            SecondaryActionButton(
                text = "Question suivante",
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                buttonHeight = 44.dp
            )
        }
    }
}
