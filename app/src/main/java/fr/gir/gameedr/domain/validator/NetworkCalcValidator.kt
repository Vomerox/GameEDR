package fr.gir.gameedr.domain.validator

import fr.gir.gameedr.domain.model.NetworkCalcExercise
import fr.gir.gameedr.domain.model.ValidationResult
import fr.gir.gameedr.domain.service.IPv4NetworkMath
import fr.gir.gameedr.domain.service.IPv4Parser

class NetworkCalcValidator {
    fun validate(
        exercise: NetworkCalcExercise,
        networkInput: String,
        broadcastInput: String
    ): ValidationResult {
        val networkValue = IPv4Parser.parseIpToLong(networkInput)
        val broadcastValue = IPv4Parser.parseIpToLong(broadcastInput)

        if (networkValue == null || broadcastValue == null) {
            return ValidationResult(
                isCorrect = false,
                headline = "Format invalide",
                explanation = "Les reponses attendent uniquement des adresses IPv4 en decimal pointe. Verifiez chaque octet avant de valider.",
                solution = buildExpectedAnswer(exercise)
            )
        }

        val expectedNetwork = exercise.referenceNetwork.networkAddress
        val expectedBroadcast = exercise.referenceNetwork.broadcastAddress
        val prefix = exercise.referenceNetwork.prefixLength
        val (octetIndex, step) = IPv4NetworkMath.subnetStep(prefix)

        if (networkValue == expectedNetwork && broadcastValue == expectedBroadcast) {
            return ValidationResult(
                isCorrect = true,
                headline = "Bonne reponse",
                explanation = "Le reseau et le broadcast correspondent bien au bloc IPv4 identifie. Les bits hote ont ete remis a zero pour le reseau puis a un pour le broadcast."
            )
        }

        val issues = buildList {
            if (networkValue != expectedNetwork) {
                add("l'adresse reseau est incorrecte")
            }
            if (broadcastValue != expectedBroadcast) {
                add("l'adresse de broadcast est incorrecte")
            }
        }.joinToString(" et ")

        return ValidationResult(
            isCorrect = false,
            headline = issues.replaceFirstChar { it.uppercase() },
            explanation = "Avec un /$prefix, le bloc avance par pas de $step dans l'octet $octetIndex. Il faut d'abord trouver le debut du bloc pour le reseau, puis prendre la derniere adresse du meme bloc pour le broadcast.",
            solution = buildExpectedAnswer(exercise)
        )
    }

    private fun buildExpectedAnswer(exercise: NetworkCalcExercise): String {
        return "Reseau : ${exercise.referenceNetwork.networkAddressText}\n" +
            "Broadcast : ${exercise.referenceNetwork.broadcastAddressText}"
    }
}
