package fr.gir.gameedr.domain.model

data class SubnettingExercise(
    val prompt: String,
    val baseNetwork: IPv4Network,
    val subnetCount: Int,
    val expectedPrefix: Int,
    val expectedSubnets: List<IPv4Network>
)
