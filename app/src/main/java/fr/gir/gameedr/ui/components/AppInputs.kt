package fr.gir.gameedr.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import fr.gir.gameedr.ui.theme.Brass
import fr.gir.gameedr.ui.theme.Sand
import fr.gir.gameedr.ui.theme.SandLight
import fr.gir.gameedr.ui.theme.SignalGreen
import fr.gir.gameedr.ui.theme.SignalRed
import fr.gir.gameedr.ui.theme.TacticalOlive

enum class FieldState {
    Neutral,
    Correct,
    Error
}

@Composable
fun MilitaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Uri,
    fieldState: FieldState = FieldState.Neutral
) {
    val borderColor = when (fieldState) {
        FieldState.Neutral -> TacticalOlive
        FieldState.Correct -> SignalGreen
        FieldState.Error -> SignalRed
    }

    val labelColor = when (fieldState) {
        FieldState.Neutral -> Sand
        FieldState.Correct -> SignalGreen
        FieldState.Error -> SignalRed
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = SandLight.copy(alpha = 0.4f)
            )
        },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = SandLight,
            unfocusedTextColor = SandLight,
            focusedBorderColor = if (fieldState == FieldState.Neutral) Brass else borderColor,
            unfocusedBorderColor = borderColor,
            focusedLabelColor = if (fieldState == FieldState.Neutral) Brass else labelColor,
            unfocusedLabelColor = labelColor,
            cursorColor = Brass,
            focusedPlaceholderColor = SandLight.copy(alpha = 0.4f),
            unfocusedPlaceholderColor = SandLight.copy(alpha = 0.32f)
        )
    )
}
