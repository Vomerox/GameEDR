package fr.gir.gameedr.domain.model

data class NetworkCalcExercise(
    val prompt: String,
    val hostIp: String,
    val maskLabel: String,
    val referenceNetwork: IPv4Network
)
