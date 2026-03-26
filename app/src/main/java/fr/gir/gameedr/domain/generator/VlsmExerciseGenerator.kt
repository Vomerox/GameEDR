package fr.gir.gameedr.domain.generator

import fr.gir.gameedr.domain.model.DifficultyLevel
import fr.gir.gameedr.domain.model.IPv4Network
import fr.gir.gameedr.domain.model.VlsmAllocation
import fr.gir.gameedr.domain.model.VlsmExercise
import fr.gir.gameedr.domain.model.VlsmRequirement
import fr.gir.gameedr.domain.service.IPv4NetworkMath
import kotlin.random.Random

class VlsmExerciseGenerator(
    private val random: Random = Random.Default
) {
    private val labels = listOf(
        "Alpha",
        "Bravo",
        "Charlie",
        "Delta",
        "Echo",
        "Foxtrot"
    )

    fun generate(level: DifficultyLevel): VlsmExercise {
        repeat(200) {
            val basePrefix = when (level) {
                DifficultyLevel.LEVEL_1 -> listOf(24, 23).random(random)
                DifficultyLevel.LEVEL_2 -> listOf(23, 22).random(random)
                DifficultyLevel.LEVEL_3 -> listOf(22, 21).random(random)
                DifficultyLevel.LEVEL_4 -> listOf(21, 20).random(random)
            }

            val requirementCount = when (level) {
                DifficultyLevel.LEVEL_1 -> 2
                DifficultyLevel.LEVEL_2 -> 3
                DifficultyLevel.LEVEL_3 -> 4
                DifficultyLevel.LEVEL_4 -> 5
            }

            val requirements = buildRequirements(level, requirementCount)
            val baseNetwork = IPv4NetworkMath.randomPrivateNetwork(basePrefix, random)
            val expectedAllocations = allocate(baseNetwork, requirements) ?: return@repeat

            return VlsmExercise(
                prompt = "Allouez un bloc principal à chaque besoin. Donnez l'adresse réseau et le masque de chaque bloc.",
                baseNetwork = baseNetwork,
                requirements = requirements.shuffled(random),
                expectedAllocations = expectedAllocations
            )
        }

        error("Impossible de generer un exercice VLSM valide.")
    }

    private fun buildRequirements(
        level: DifficultyLevel,
        count: Int
    ): List<VlsmRequirement> {
        val hostPools = when (level) {
            DifficultyLevel.LEVEL_1 -> listOf(12, 18, 24, 30, 50, 60)
            DifficultyLevel.LEVEL_2 -> listOf(14, 20, 28, 45, 62, 90, 110)
            DifficultyLevel.LEVEL_3 -> listOf(14, 30, 46, 62, 94, 126, 180)
            DifficultyLevel.LEVEL_4 -> listOf(14, 30, 50, 94, 126, 180, 220, 250)
        }

        return labels.shuffled(random)
            .take(count)
            .mapIndexed { index, label ->
                val pool = hostPools.shuffled(random)
                VlsmRequirement(label = label, hostCount = pool[index % pool.size])
            }
    }

    private fun allocate(
        baseNetwork: IPv4Network,
        requirements: List<VlsmRequirement>
    ): List<VlsmAllocation>? {
        val sortedRequirements = requirements.sortedByDescending { it.hostCount }
        val allocations = mutableListOf<VlsmAllocation>()
        var cursor = baseNetwork.networkAddress

        for (requirement in sortedRequirements) {
            val prefix = IPv4NetworkMath.requiredPrefixForHosts(requirement.hostCount)
            var startAddress = IPv4NetworkMath.networkAddressOf(cursor, prefix)
            if (startAddress < cursor) {
                startAddress += IPv4NetworkMath.blockSize(prefix)
            }

            if (!IPv4NetworkMath.isAligned(startAddress, prefix)) {
                return null
            }

            val candidate = IPv4Network(startAddress, prefix)
            if (!baseNetwork.contains(candidate)) {
                return null
            }

            allocations += VlsmAllocation(label = requirement.label, network = candidate)
            cursor = candidate.broadcastAddress + 1L
        }

        return allocations
    }
}
