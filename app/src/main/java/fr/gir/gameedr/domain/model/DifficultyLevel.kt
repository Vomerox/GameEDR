package fr.gir.gameedr.domain.model

enum class DifficultyLevel(
    val value: Int,
    val title: String,
    val briefing: String
) {
    LEVEL_1(
        value = 1,
        title = "Niveau 1",
        briefing = "Fondamentaux et blocs simples."
    ),
    LEVEL_2(
        value = 2,
        title = "Niveau 2",
        briefing = "Masques variés et calculs intermédiaires."
    ),
    LEVEL_3(
        value = 3,
        title = "Niveau 3",
        briefing = "Raisonnement plus large sur plusieurs sous-réseaux."
    ),
    LEVEL_4(
        value = 4,
        title = "Niveau 4",
        briefing = "Situation opérationnelle avec alignements plus fins."
    );

    companion object {
        fun fromValue(value: Int): DifficultyLevel {
            return entries.firstOrNull { it.value == value } ?: LEVEL_1
        }
    }
}
