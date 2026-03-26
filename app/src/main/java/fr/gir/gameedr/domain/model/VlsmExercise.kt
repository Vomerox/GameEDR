package fr.gir.gameedr.domain.model

data class VlsmRequirement(
    val label: String,
    val hostCount: Int
)

data class VlsmAllocation(
    val label: String,
    val network: IPv4Network
)

data class VlsmExercise(
    val prompt: String,
    val baseNetwork: IPv4Network,
    val requirements: List<VlsmRequirement>,
    val expectedAllocations: List<VlsmAllocation>
)
