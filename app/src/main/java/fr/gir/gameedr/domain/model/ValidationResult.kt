package fr.gir.gameedr.domain.model

data class ValidationResult(
    val isCorrect: Boolean,
    val headline: String,
    val explanation: String,
    val solution: String? = null
)
