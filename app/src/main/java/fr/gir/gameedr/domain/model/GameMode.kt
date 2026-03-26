package fr.gir.gameedr.domain.model

enum class GameMode(
    val routeSegment: String,
    val title: String,
    val description: String
) {
    NETWORK_CALC(
        routeSegment = "calcul-reseau",
        title = "Calcul d'adresse réseau et broadcast",
        description = "Retrouver l'adresse réseau et le broadcast d'un hôte IPv4."
    ),
    SUBNETTING(
        routeSegment = "sous-reseau",
        title = "Sous-réseau",
        description = "Découper un réseau en sous-réseaux égaux et alignés."
    ),
    VLSM(
        routeSegment = "vlsm",
        title = "VLSM",
        description = "Allouer les blocs principaux en respectant tri, masque et alignement."
    );

    companion object {
        fun fromRouteSegment(routeSegment: String): GameMode {
            return entries.firstOrNull { it.routeSegment == routeSegment } ?: NETWORK_CALC
        }
    }
}
