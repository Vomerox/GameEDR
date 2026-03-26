package fr.gir.gameedr.data

import fr.gir.gameedr.domain.generator.NetworkCalcExerciseGenerator
import fr.gir.gameedr.domain.generator.SubnettingExerciseGenerator
import fr.gir.gameedr.domain.generator.VlsmExerciseGenerator
import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.NetworkCalcExercise
import fr.gir.gameedr.domain.model.SubnettingExercise
import fr.gir.gameedr.domain.model.ValidationResult
import fr.gir.gameedr.domain.model.VlsmExercise
import fr.gir.gameedr.domain.validator.NetworkCalcValidator
import fr.gir.gameedr.domain.validator.SubnettingValidator
import fr.gir.gameedr.domain.validator.VlsmValidator

class ExerciseRepository(
    private val networkCalcExerciseGenerator: NetworkCalcExerciseGenerator = NetworkCalcExerciseGenerator(),
    private val subnettingExerciseGenerator: SubnettingExerciseGenerator = SubnettingExerciseGenerator(),
    private val vlsmExerciseGenerator: VlsmExerciseGenerator = VlsmExerciseGenerator(),
    private val networkCalcValidator: NetworkCalcValidator = NetworkCalcValidator(),
    private val subnettingValidator: SubnettingValidator = SubnettingValidator(),
    private val vlsmValidator: VlsmValidator = VlsmValidator()
) {
    fun nextNetworkCalcExercise(level: DifficultyLevel): NetworkCalcExercise {
        return networkCalcExerciseGenerator.generate(level)
    }

    fun validateNetworkCalc(
        exercise: NetworkCalcExercise,
        networkInput: String,
        broadcastInput: String
    ): ValidationResult {
        return networkCalcValidator.validate(exercise, networkInput, broadcastInput)
    }

    fun nextSubnettingExercise(level: DifficultyLevel): SubnettingExercise {
        return subnettingExerciseGenerator.generate(level)
    }

    fun validateSubnetting(
        exercise: SubnettingExercise,
        maskInput: String,
        subnetInputs: List<String>
    ): ValidationResult {
        return subnettingValidator.validate(exercise, maskInput, subnetInputs)
    }

    fun nextVlsmExercise(level: DifficultyLevel): VlsmExercise {
        return vlsmExerciseGenerator.generate(level)
    }

    fun validateVlsm(
        exercise: VlsmExercise,
        networkInputs: Map<String, String>,
        maskInputs: Map<String, String>
    ): ValidationResult {
        return vlsmValidator.validate(exercise, networkInputs, maskInputs)
    }
}
