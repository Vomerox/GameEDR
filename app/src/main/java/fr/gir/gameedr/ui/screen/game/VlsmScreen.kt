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
fun VlsmScreen(
    level: DifficultyLevel,
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    val repository = remember { ExerciseRepository() }
    var exercise by remember(level) { mutableStateOf(repository.nextVlsmExercise(level)) }
    var networkInputs by remember(level) { mutableStateOf(exercise.requirements.associate { it.label to "" }) }
    var maskInputs by remember(level) { mutableStateOf(exercise.requirements.associate { it.label to "" }) }
    var validationResult by remember(level) { mutableStateOf<ValidationResult?>(null) }
    var showMethod by remember(level) { mutableStateOf(false) }

    fun updateNetwork(label: String, value: String) {
        networkInputs = networkInputs.toMutableMap().apply { put(label, value) }
    }

    fun updateMask(label: String, value: String) {
        maskInputs = maskInputs.toMutableMap().apply { put(label, value) }
    }

    fun nextExercise() {
        exercise = repository.nextVlsmExercise(level)
        networkInputs = exercise.requirements.associate { it.label to "" }
        maskInputs = exercise.requirements.associate { it.label to "" }
        validationResult = null
    }

    val expectedByLabel = remember(exercise) { exercise.expectedAllocations.associateBy { it.label } }
    val displayedRequirements = remember(exercise, level) {
        if (level.value >= 3) {
            exercise.requirements
        } else {
            exercise.requirements.sortedByDescending { it.hostCount }
        }
    }

    AppScaffold(
        title = "VLSM",
        subtitle = level.title,
        onHome = onHome,
        homeAsNavigationIcon = true,
        bottomBar = {
            GameActionDock(
                onValidate = {
                    validationResult = repository.validateVlsm(
                        exercise = exercise,
                        networkInputs = networkInputs,
                        maskInputs = maskInputs
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
                    "Besoins : ${exercise.requirements.size}",
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
                    text = if (level.value >= 3) {
                        "Les besoins peuvent appara\u00EEtre dans un ordre non tri\u00E9. Il faut d'abord les classer avant de poser les blocs."
                    } else {
                        "Les besoins sont affich\u00E9s du plus grand au plus petit pour faciliter l'allocation VLSM."
                    },
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
                        text = "1. Trier les besoins du plus grand au plus petit.\n2. Choisir le plus petit masque suffisant pour chaque bloc.\n3. Poser chaque r\u00E9seau sur la premi\u00E8re fronti\u00E8re disponible sans chevauchement.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                displayedRequirements.forEach { requirement ->
                    val expectedNetwork = expectedByLabel.getValue(requirement.label).network
                    val networkState = when {
                        validationResult == null || networkInputs[requirement.label].isNullOrBlank() -> FieldState.Neutral
                        networkInputs[requirement.label].orEmpty().trim() == expectedNetwork.networkAddressText -> FieldState.Correct
                        else -> FieldState.Error
                    }
                    val maskState = when {
                        validationResult == null || maskInputs[requirement.label].isNullOrBlank() -> FieldState.Neutral
                        IPv4Parser.parseMaskOrCidrToPrefix(maskInputs[requirement.label].orEmpty()) == expectedNetwork.prefixLength -> FieldState.Correct
                        else -> FieldState.Error
                    }

                    SectionCard {
                        Text(
                            text = "${requirement.label} - ${requirement.hostCount} h\u00F4tes",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        MilitaryTextField(
                            value = networkInputs[requirement.label].orEmpty(),
                            onValueChange = { updateNetwork(requirement.label, it) },
                            label = "Adresse r\u00E9seau",
                            placeholder = "10.0.0.0",
                            keyboardType = KeyboardType.Uri,
                            fieldState = networkState
                        )
                        MilitaryTextField(
                            value = maskInputs[requirement.label].orEmpty(),
                            onValueChange = { updateMask(requirement.label, it) },
                            label = "Masque",
                            placeholder = "/26 ou 255.255.255.192",
                            keyboardType = KeyboardType.Uri,
                            fieldState = maskState
                        )
                    }
                }
            }
            validationResult?.let { result ->
                CorrectionCard(result = result)
            }
        }
    }
}
