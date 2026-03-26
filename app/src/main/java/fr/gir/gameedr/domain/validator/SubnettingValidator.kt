package fr.gir.gameedr.domain.validator

import fr.gir.gameedr.domain.model.IPv4Network
import fr.gir.gameedr.domain.model.SubnettingExercise
import fr.gir.gameedr.domain.model.ValidationResult
import fr.gir.gameedr.domain.service.IPv4NetworkMath
import fr.gir.gameedr.domain.service.IPv4Parser

class SubnettingValidator {
    fun validate(
        exercise: SubnettingExercise,
        maskInput: String,
        subnetInputs: List<String>
    ): ValidationResult {
        val prefix = IPv4Parser.parseMaskOrCidrToPrefix(maskInput)
        if (prefix == null) {
            return ValidationResult(
                isCorrect = false,
                headline = "Masque invalide",
                explanation = "Le masque doit etre saisi en CIDR ou en decimal. Reprenez le nombre de bits empruntes avant de lister les sous-reseaux.",
                solution = buildSolution(exercise)
            )
        }

        if (subnetInputs.any { it.isBlank() }) {
            return ValidationResult(
                isCorrect = false,
                headline = "Sous-reseaux incomplets",
                explanation = "Chaque sous-reseau demande une adresse reseau. Une adresse manquante empeche de verifier la coherence de l'ensemble.",
                solution = buildSolution(exercise)
            )
        }

        val parsedAddresses = subnetInputs.map { IPv4Parser.parseIpToLong(it) }
        if (parsedAddresses.any { it == null }) {
            return ValidationResult(
                isCorrect = false,
                headline = "Adresse IPv4 invalide",
                explanation = "Les sous-reseaux attendent des adresses reseau au format decimal pointe. Evitez de saisir une plage d'hotes ou une notation abregee.",
                solution = buildSolution(exercise)
            )
        }

        if (prefix != exercise.expectedPrefix) {
            val borrowedBits = exercise.expectedPrefix - exercise.baseNetwork.prefixLength
            return ValidationResult(
                isCorrect = false,
                headline = "Mauvais masque",
                explanation = "Pour obtenir ${exercise.subnetCount} sous-reseaux egaux, il faut emprunter $borrowedBits bit(s) au champ hote. Le masque resultat doit donc correspondre a /${exercise.expectedPrefix}.",
                solution = buildSolution(exercise)
            )
        }

        val userNetworks = parsedAddresses.filterNotNull().map { address ->
            IPv4Network.fromAnyAddress(address, prefix)
        }

        if (parsedAddresses.filterNotNull().zip(userNetworks).any { (address, network) -> address != network.networkAddress }) {
            return ValidationResult(
                isCorrect = false,
                headline = "Confusion plage / reseau",
                explanation = "Une ou plusieurs valeurs saisies ne sont pas des adresses reseau alignees. Une adresse reseau doit etre le debut exact du bloc, pas une adresse hote ni un broadcast.",
                solution = buildSolution(exercise)
            )
        }

        val distinctUserAddresses = userNetworks.map { it.networkAddress }.distinct()
        if (distinctUserAddresses.size != exercise.subnetCount) {
            return ValidationResult(
                isCorrect = false,
                headline = "Sous-reseaux incoherents",
                explanation = "Le nombre de sous-reseaux distincts ne correspond pas a la demande. Chaque bloc doit apparaitre une seule fois et couvrir une portion differente du reseau de depart.",
                solution = buildSolution(exercise)
            )
        }

        val expectedAddresses = exercise.expectedSubnets.map { it.networkAddress }.sorted()
        val providedAddresses = distinctUserAddresses.sorted()
        if (providedAddresses != expectedAddresses) {
            val (octetIndex, step) = IPv4NetworkMath.subnetStep(prefix)
            return ValidationResult(
                isCorrect = false,
                headline = "Sous-reseaux mal positionnes",
                explanation = "Une fois le bon masque choisi, chaque adresse reseau doit avancer par pas de $step dans l'octet $octetIndex. Il faut lister uniquement les debuts de blocs alignes dans le reseau de depart.",
                solution = buildSolution(exercise)
            )
        }

        return ValidationResult(
            isCorrect = true,
            headline = "Bonne reponse",
            explanation = "Le masque et les adresses reseau sont coherents. Chaque sous-reseau est bien aligne et couvre une part egale du bloc initial."
        )
    }

    private fun buildSolution(exercise: SubnettingExercise): String {
        val networks = exercise.expectedSubnets.joinToString(separator = "\n") { subnet ->
            subnet.networkAddressText
        }
        return "Masque attendu : /${exercise.expectedPrefix} (${IPv4NetworkMath.longToIp(IPv4NetworkMath.prefixToMask(exercise.expectedPrefix))})\n" +
            "Adresses reseau :\n$networks"
    }
}
