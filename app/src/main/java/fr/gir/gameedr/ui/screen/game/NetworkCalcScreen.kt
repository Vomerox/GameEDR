package fr.gir.gameedr.ui.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.gir.gameedr.data.ExerciseRepository
import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.ValidationResult
import fr.gir.gameedr.ui.components.AppScaffold
import fr.gir.gameedr.ui.components.CompactActionButton
import fr.gir.gameedr.ui.components.CorrectionCard
import fr.gir.gameedr.ui.components.FieldState
import fr.gir.gameedr.ui.components.GameActionDock
import fr.gir.gameedr.ui.components.LogoHeader
import fr.gir.gameedr.ui.components.MilitaryTextField
import fr.gir.gameedr.ui.components.QuestionCard
import fr.gir.gameedr.ui.components.SectionCard

@Suppress("UNUSED_PARAMETER")
@Composable
fun NetworkCalcScreen(
    level: DifficultyLevel,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val repository = remember { ExerciseRepository() }
    var exercise by remember(level) { mutableStateOf(repository.nextNetworkCalcExercise(level)) }
    var networkInput by remember(level) { mutableStateOf("") }
    var broadcastInput by remember(level) { mutableStateOf("") }
    var validationResult by remember(level) { mutableStateOf<ValidationResult?>(null) }
    var showMethod by remember(level) { mutableStateOf(false) }

    fun nextExercise() {
        exercise = repository.nextNetworkCalcExercise(level)
        networkInput = ""
        broadcastInput = ""
        validationResult = null
    }

    val expectedNetwork = exercise.referenceNetwork.networkAddressText
    val expectedBroadcast = exercise.referenceNetwork.broadcastAddressText
    val networkState = inputState(validationResult, networkInput, expectedNetwork)
    val broadcastState = inputState(validationResult, broadcastInput, expectedBroadcast)

    AppScaffold(
        title = "Calcul r\u00E9seau",
        subtitle = level.title,
        onHome = onHome,
        homeAsNavigationIcon = true,
        bottomBar = {
            GameActionDock(
                onValidate = {
                    validationResult = repository.validateNetworkCalc(
                        exercise = exercise,
                        networkInput = networkInput,
                        broadcastInput = broadcastInput
                    )
                },
                onNext = ::nextExercise
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LogoHeader(size = 52.dp)
            QuestionCard(
                title = "Ordre de mission",
                prompt = exercise.prompt,
                metadata = listOf(
                    "H\u00F4te : ${exercise.hostIp}",
                    "Masque : ${exercise.maskLabel}",
                    "Niveau ${level.value}"
                )
            )
            SectionCard {
                Text(
                    text = "R\u00E9ponse op\u00E9rateur",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Saisir uniquement des adresses IPv4 en d\u00E9cimal point\u00E9.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CompactActionButton(
                        text = if (showMethod) "Masquer m\u00E9thode" else "Afficher m\u00E9thode",
                        onClick = { showMethod = !showMethod },
                        buttonHeight = 36.dp
                    )
                }
                if (showMethod) {
                    Text(
                        text = "M\u00E9thode",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "1. Localiser le bloc gr\u00E2ce au masque.\n2. Mettre les bits h\u00F4te \u00E0 z\u00E9ro pour trouver le r\u00E9seau.\n3. Mettre les bits h\u00F4te \u00E0 un pour trouver le broadcast.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                MilitaryTextField(
                    value = networkInput,
                    onValueChange = { networkInput = it },
                    label = "Adresse r\u00E9seau",
                    placeholder = "192.168.10.0",
                    keyboardType = KeyboardType.Uri,
                    fieldState = networkState
                )
                MilitaryTextField(
                    value = broadcastInput,
                    onValueChange = { broadcastInput = it },
                    label = "Broadcast",
                    placeholder = "192.168.10.63",
                    keyboardType = KeyboardType.Uri,
                    fieldState = broadcastState
                )
            }
            validationResult?.let { result ->
                CorrectionCard(result = result)
            }
        }
    }
}

private fun inputState(
    validationResult: ValidationResult?,
    value: String,
    expected: String
): FieldState {
    return when {
        validationResult == null || value.isBlank() -> FieldState.Neutral
        value.trim() == expected -> FieldState.Correct
        else -> FieldState.Error
    }
}
