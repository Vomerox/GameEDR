package fr.gir.gameedr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.ui.theme.Brass
import fr.gir.gameedr.ui.theme.BrassDeep
import fr.gir.gameedr.ui.theme.DarkSoil
import fr.gir.gameedr.ui.theme.SandLight
import fr.gir.gameedr.ui.theme.TacticalOlive
import fr.gir.gameedr.ui.theme.TacticalOliveSoft

@Composable
fun PrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    enabled: Boolean = true,
    buttonHeight: Dp = 56.dp
) {
    val resolvedModifier = if (fullWidth) modifier.fillMaxWidth() else modifier

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = resolvedModifier.height(buttonHeight),
        shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
        border = BorderStroke(1.dp, BrassDeep),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Brass,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun SecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    enabled: Boolean = true,
    buttonHeight: Dp = 52.dp
) {
    val resolvedModifier = if (fullWidth) modifier.fillMaxWidth() else modifier

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = resolvedModifier.height(buttonHeight),
        shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp),
        border = BorderStroke(1.dp, TacticalOliveSoft),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkSoil.copy(alpha = 0.4f),
            contentColor = SandLight
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun CompactActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 40.dp
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(buttonHeight),
        shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp),
        border = BorderStroke(1.dp, TacticalOlive),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkSoil.copy(alpha = 0.35f),
            contentColor = SandLight
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
