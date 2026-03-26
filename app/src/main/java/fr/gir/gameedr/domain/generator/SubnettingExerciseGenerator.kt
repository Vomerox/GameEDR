package fr.gir.gameedr.domain.generator

import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.SubnettingExercise
import fr.gir.gameedr.domain.service.IPv4NetworkMath
import kotlin.random.Random

class SubnettingExerciseGenerator(
    private val random: Random = Random.Default
) {
    fun generate(level: DifficultyLevel): SubnettingExercise {
        val basePrefix = when (level) {
            DifficultyLevel.LEVEL_1 -> listOf(24, 23).random(random)
            DifficultyLevel.LEVEL_2 -> listOf(24, 23, 22).random(random)
            DifficultyLevel.LEVEL_3 -> listOf(23, 22, 21).random(random)
            DifficultyLevel.LEVEL_4 -> listOf(22, 21, 20).random(random)
        }

        val subnetCount = when (level) {
            DifficultyLevel.LEVEL_1 -> listOf(2, 4).random(random)
            DifficultyLevel.LEVEL_2 -> listOf(4, 8).random(random)
            DifficultyLevel.LEVEL_3 -> listOf(4, 8).random(random)
            DifficultyLevel.LEVEL_4 -> 8
        }

        val expectedPrefix = basePrefix + subnetCount.countTrailingZeroBits()
        val baseNetwork = IPv4NetworkMath.randomPrivateNetwork(basePrefix, random)
        val expectedSubnets = IPv4NetworkMath.subnetworks(baseNetwork, expectedPrefix).take(subnetCount)

        return SubnettingExercise(
            prompt = "Découpez ce réseau en sous-réseaux égaux. Donnez le nouveau masque puis chaque adresse réseau.",
            baseNetwork = baseNetwork,
            subnetCount = subnetCount,
            expectedPrefix = expectedPrefix,
            expectedSubnets = expectedSubnets
        )
    }
}
