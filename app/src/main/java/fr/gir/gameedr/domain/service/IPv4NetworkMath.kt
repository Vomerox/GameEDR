package fr.gir.gameedr.domain.service

import fr.gir.gameedr.domain.model.IPv4Network
import kotlin.math.max
import kotlin.random.Random

object IPv4NetworkMath {
    private const val IPv4MaxMask = 0xFFFFFFFFL

    fun toAddress(octet1: Int, octet2: Int, octet3: Int, octet4: Int): Long {
        return ((octet1.toLong() and 0xFF) shl 24) or
            ((octet2.toLong() and 0xFF) shl 16) or
            ((octet3.toLong() and 0xFF) shl 8) or
            (octet4.toLong() and 0xFF)
    }

    fun longToIp(value: Long): String {
        return listOf(
            (value shr 24) and 0xFF,
            (value shr 16) and 0xFF,
            (value shr 8) and 0xFF,
            value and 0xFF
        ).joinToString(".")
    }

    fun prefixToMask(prefixLength: Int): Long {
        require(prefixLength in 0..32) { "Prefixe invalide." }
        if (prefixLength == 0) {
            return 0L
        }
        return ((-1L shl (32 - prefixLength)) and IPv4MaxMask)
    }

    fun maskToPrefix(maskValue: Long): Int? {
        for (prefix in 0..32) {
            if (prefixToMask(prefix) == maskValue) {
                return prefix
            }
        }
        return null
    }

    fun blockSize(prefixLength: Int): Long {
        return 1L shl (32 - prefixLength)
    }

    fun usableHostCapacity(prefixLength: Int): Long {
        return max(0L, blockSize(prefixLength) - 2L)
    }

    fun networkAddressOf(address: Long, prefixLength: Int): Long {
        return address and prefixToMask(prefixLength)
    }

    fun broadcastAddressOf(address: Long, prefixLength: Int): Long {
        return networkAddressOf(address, prefixLength) or (prefixToMask(prefixLength).inv() and IPv4MaxMask)
    }

    fun isAligned(address: Long, prefixLength: Int): Boolean {
        return address == networkAddressOf(address, prefixLength)
    }

    fun requiredPrefixForHosts(hostCount: Int): Int {
        require(hostCount > 0) { "Le nombre d'hotes doit etre positif." }
        for (prefix in 30 downTo 0) {
            if (usableHostCapacity(prefix) >= hostCount) {
                return prefix
            }
        }
        return 0
    }

    fun displayMask(prefixLength: Int, useCidr: Boolean): String {
        return if (useCidr) {
            "/$prefixLength"
        } else {
            longToIp(prefixToMask(prefixLength))
        }
    }

    fun subnetStep(prefixLength: Int): Pair<Int, Int> {
        val octets = longToIp(prefixToMask(prefixLength)).split(".").map { it.toInt() }
        val partialIndex = octets.indexOfFirst { it in 1..254 }
        if (partialIndex != -1) {
            return (partialIndex + 1) to (256 - octets[partialIndex])
        }

        val zeroIndex = octets.indexOfFirst { it == 0 }
        if (zeroIndex != -1) {
            return (zeroIndex + 1) to 256
        }

        return 4 to 1
    }

    fun subnetworks(baseNetwork: IPv4Network, newPrefix: Int): List<IPv4Network> {
        require(newPrefix >= baseNetwork.prefixLength) { "Le nouveau prefixe doit etre plus specifique." }
        val subnetCount = 1 shl (newPrefix - baseNetwork.prefixLength)
        val increment = blockSize(newPrefix)
        return buildList(subnetCount) {
            var cursor = baseNetwork.networkAddress
            repeat(subnetCount) {
                add(IPv4Network(cursor, newPrefix))
                cursor += increment
            }
        }
    }

    fun randomPrivateNetwork(prefixLength: Int, random: Random): IPv4Network {
        return IPv4Network.fromAnyAddress(randomPrivateAddress(random), prefixLength)
    }

    private fun randomPrivateAddress(random: Random): Long {
        return when (random.nextInt(3)) {
            0 -> toAddress(
                10,
                random.nextInt(0, 256),
                random.nextInt(0, 256),
                random.nextInt(0, 256)
            )

            1 -> toAddress(
                172,
                random.nextInt(16, 32),
                random.nextInt(0, 256),
                random.nextInt(0, 256)
            )

            else -> toAddress(
                192,
                168,
                random.nextInt(0, 256),
                random.nextInt(0, 256)
            )
        }
    }
}
