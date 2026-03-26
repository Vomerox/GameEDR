package fr.gir.gameedr.domain.validator

import fr.gir.gameedr.domain.model.IPv4Network
import fr.gir.gameedr.domain.model.ValidationResult
import fr.gir.gameedr.domain.model.VlsmExercise
import fr.gir.gameedr.domain.service.IPv4Parser

class VlsmValidator {
    fun validate(
        exercise: VlsmExercise,
        networkInputs: Map<String, String>,
        maskInputs: Map<String, String>
    ): ValidationResult {
        val submittedNetworks = mutableListOf<Pair<String, IPv4Network>>()

        for (requirement in exercise.requirements) {
            val networkRaw = networkInputs[requirement.label].orEmpty()
            val maskRaw = maskInputs[requirement.label].orEmpty()
            val networkAddress = IPv4Parser.parseIpToLong(networkRaw)
            val prefix = IPv4Parser.parseMaskOrCidrToPrefix(maskRaw)

            if (networkAddress == null || prefix == null) {
                return ValidationResult(
                    isCorrect = false,
                    headline = "Saisie invalide",
                    explanation = "Chaque bloc VLSM attend une adresse reseau en decimal pointe et un masque en CIDR ou en decimal. Une saisie incomplete empeche de verifier l'allocation.",
                    solution = buildSolution(exercise)
                )
            }

            val normalized = IPv4Network.fromAnyAddress(networkAddress, prefix)
            if (normalized.networkAddress != networkAddress) {
                return ValidationResult(
                    isCorrect = false,
                    headline = "Alignement incorrect",
                    explanation = "Une adresse proposee n'est pas alignee sur son masque. En VLSM, chaque bloc doit commencer exactement au debut d'une frontiere compatible avec son prefixe.",
                    solution = buildSolution(exercise)
                )
            }

            if (!exercise.baseNetwork.contains(normalized)) {
                return ValidationResult(
                    isCorrect = false,
                    headline = "Bloc hors du reseau de depart",
                    explanation = "Au moins un bloc depasse le reseau fourni. Toutes les allocations doivent rester incluses dans le reseau initial avant de verifier les details de tri ou de masque.",
                    solution = buildSolution(exercise)
                )
            }

            submittedNetworks += requirement.label to normalized
        }

        val sortedByStart = submittedNetworks.sortedBy { it.second.networkAddress }
        val hasOverlap = sortedByStart.zipWithNext().any { (left, right) ->
            left.second.broadcastAddress >= right.second.networkAddress
        }
        if (hasOverlap) {
            return ValidationResult(
                isCorrect = false,
                headline = "Chevauchement detecte",
                explanation = "Deux blocs se recouvrent. En VLSM, on place d'abord les plus gros besoins puis on aligne chaque bloc suivant juste apres le precedent sans recouvrement.",
                solution = buildSolution(exercise)
            )
        }

        val expectedByLabel = exercise.expectedAllocations.associateBy { it.label }
        val wrongMask = exercise.requirements.firstOrNull { requirement ->
            val userPrefix = submittedNetworks.first { it.first == requirement.label }.second.prefixLength
            val expectedPrefix = expectedByLabel.getValue(requirement.label).network.prefixLength
            userPrefix != expectedPrefix
        }
        if (wrongMask != null) {
            val expectedPrefix = expectedByLabel.getValue(wrongMask.label).network.prefixLength
            return ValidationResult(
                isCorrect = false,
                headline = "Mauvais masque",
                explanation = "Le besoin ${wrongMask.label} (${wrongMask.hostCount} hotes) demande un bloc taille au plus juste. Il faut choisir le prefixe qui couvre les hotes utiles, ici /$expectedPrefix, avant de penser au placement.",
                solution = buildSolution(exercise)
            )
        }

        val wrongPlacement = exercise.requirements.firstOrNull { requirement ->
            val userNetwork = submittedNetworks.first { it.first == requirement.label }.second
            val expectedNetwork = expectedByLabel.getValue(requirement.label).network
            userNetwork != expectedNetwork
        }
        if (wrongPlacement != null) {
            return ValidationResult(
                isCorrect = false,
                headline = "Ordre de tri ou placement incorrect",
                explanation = "Les blocs VLSM se calculent en triant d'abord les besoins du plus grand au plus petit, puis en allouant chaque reseau sur la premiere frontiere disponible. Si l'ordre change, tout le reste du plan d'adressage se decale.",
                solution = buildSolution(exercise)
            )
        }

        return ValidationResult(
            isCorrect = true,
            headline = "Bonne reponse",
            explanation = "Le tri des besoins, le choix des masques et l'alignement des blocs sont corrects. L'allocation VLSM est globale et coherente."
        )
    }

    private fun buildSolution(exercise: VlsmExercise): String {
        val expected = exercise.expectedAllocations.joinToString(separator = "\n") { allocation ->
            "${allocation.label} : ${allocation.network.networkAddressText} /${allocation.network.prefixLength} (${allocation.network.maskText})"
        }
        return "Bonne approche : trier les besoins du plus grand au plus petit, choisir le masque minimal, puis aligner chaque bloc.\n$expected"
    }
}
