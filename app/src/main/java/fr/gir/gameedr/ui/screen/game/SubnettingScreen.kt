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
import fr.gir.gameedr.domain.service.IPv4Parser
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
fun SubnettingScreen(
    level: DifficultyLevel,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val repository = remember { ExerciseRepository() }
    var exercise by remember(level) { mutableStateOf(repository.nextSubnettingExercise(level)) }
    var maskInput by remember(level) { mutableStateOf("") }
    var subnetInputs by remember(level) { mutableStateOf(List(exercise.subnetCount) { "" }) }
    var validationResult by remember(level) { mutableStateOf<ValidationResult?>(null) }
    var showMethod by remember(level) { mutableStateOf(false) }

    fun updateSubnet(index: Int, value: String) {
        subnetInputs = subnetInputs.toMutableList().apply { set(index, value) }
    }

    fun nextExercise() {
        exercise = repository.nextSubnettingExercise(level)
        maskInput = ""
        subnetInputs = List(exercise.subnetCount) { "" }
        validationResult = null
    }

    val expectedPrefix = exercise.expectedPrefix
    val expectedAddresses = remember(exercise) { exercise.expectedSubnets.map { it.networkAddressText }.toSet() }
    val maskState = when {
        validationResult == null || maskInput.isBlank() -> FieldState.Neutral
        IPv4Parser.parseMaskOrCidrToPrefix(maskInput) == expectedPrefix -> FieldState.Correct
        else -> FieldState.Error
    }

    AppScaffold(
        title = "Sous-r\u00E9seau",
        subtitle = level.title,
        onHome = onHome,
        homeAsNavigationIcon = true,
        bottomBar = {
            GameActionDock(
                onValidate = {
                    validationResult = repository.validateSubnetting(
                        exercise = exercise,
                        maskInput = maskInput,
                        subnetInputs = subnetInputs
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
                    "R\u00E9seau de d\u00E9part : ${exercise.baseNetwork.cidr}",
                    "Sous-r\u00E9seaux demand\u00E9s : ${exercise.subnetCount}",
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
                    text = "Donner le nouveau masque puis une adresse r\u00E9seau par bloc.",
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
                        text = "1. Emprunter assez de bits pour obtenir le nombre demand\u00E9.\n2. Convertir ce nombre en nouveau masque.\n3. Lister les adresses r\u00E9seau avec un pas constant.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                MilitaryTextField(
                    value = maskInput,
                    onValueChange = { maskInput = it },
                    label = "Nouveau masque",
                    placeholder = "/26 ou 255.255.255.192",
                    keyboardType = KeyboardType.Uri,
                    fieldState = maskState
                )
                subnetInputs.forEachIndexed { index, value ->
                    val state = when {
                        validationResult == null || value.isBlank() -> FieldState.Neutral
                        expectedAddresses.contains(value.trim()) -> FieldState.Correct
                        else -> FieldState.Error
                    }
                    MilitaryTextField(
                        value = value,
                        onValueChange = { updateSubnet(index, it) },
                        label = "Sous-r\u00E9seau ${index + 1}",
                        placeholder = "192.168.10.0",
                        keyboardType = KeyboardType.Uri,
                        fieldState = state
                    )
                }
            }
            validationResult?.let { result ->
                CorrectionCard(result = result)
            }
        }
    }
}
