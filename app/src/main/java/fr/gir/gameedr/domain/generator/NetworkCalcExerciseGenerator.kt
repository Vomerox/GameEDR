package fr.gir.gameedr.domain.generator

import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.NetworkCalcExercise
import fr.gir.gameedr.domain.service.IPv4NetworkMath
import kotlin.random.Random

class NetworkCalcExerciseGenerator(
    private val random: Random = Random.Default
) {
    fun generate(level: DifficultyLevel): NetworkCalcExercise {
        val prefix = when (level) {
            DifficultyLevel.LEVEL_1 -> listOf(24, 25, 26, 27).random(random)
            DifficultyLevel.LEVEL_2 -> listOf(23, 24, 25, 26, 27, 28).random(random)
            DifficultyLevel.LEVEL_3 -> listOf(20, 21, 22, 23, 24, 26, 27, 28).random(random)
            DifficultyLevel.LEVEL_4 -> listOf(16, 18, 20, 22, 24, 27, 28, 29, 30).random(random)
        }

        val referenceNetwork = IPv4NetworkMath.randomPrivateNetwork(prefix, random)
        val hostOffset = when {
            referenceNetwork.totalAddressCount <= 2L -> 0L
            else -> random.nextLong(1L, referenceNetwork.totalAddressCount - 1L)
        }
        val hostAddress = referenceNetwork.networkAddress + hostOffset
        val useCidr = when (level) {
            DifficultyLevel.LEVEL_1 -> true
            else -> random.nextBoolean()
        }

        return NetworkCalcExercise(
            prompt = "Déterminez l'adresse réseau et l'adresse de broadcast de cet hôte IPv4.",
            hostIp = IPv4NetworkMath.longToIp(hostAddress),
            maskLabel = IPv4NetworkMath.displayMask(prefix, useCidr),
            referenceNetwork = referenceNetwork
        )
    }
}
