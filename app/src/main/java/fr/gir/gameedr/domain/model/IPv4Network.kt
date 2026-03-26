package fr.gir.gameedr.domain.model

import fr.gir.gameedr.domain.service.IPv4NetworkMath

data class IPv4Network(
    val networkAddress: Long,
    val prefixLength: Int
) {
    init {
        require(prefixLength in 0..32) { "Prefixe IPv4 invalide." }
        require(networkAddress == IPv4NetworkMath.networkAddressOf(networkAddress, prefixLength)) {
            "L'adresse doit etre alignee sur le prefixe."
        }
    }

    val broadcastAddress: Long
        get() = IPv4NetworkMath.broadcastAddressOf(networkAddress, prefixLength)

    val totalAddressCount: Long
        get() = IPv4NetworkMath.blockSize(prefixLength)

    val usableHostCount: Long
        get() = IPv4NetworkMath.usableHostCapacity(prefixLength)

    val networkAddressText: String
        get() = IPv4NetworkMath.longToIp(networkAddress)

    val broadcastAddressText: String
        get() = IPv4NetworkMath.longToIp(broadcastAddress)

    val maskText: String
        get() = IPv4NetworkMath.longToIp(IPv4NetworkMath.prefixToMask(prefixLength))

    val cidr: String
        get() = "${networkAddressText}/$prefixLength"

    fun contains(ipAddress: Long): Boolean {
        return ipAddress in networkAddress..broadcastAddress
    }

    fun contains(other: IPv4Network): Boolean {
        return other.networkAddress >= networkAddress && other.broadcastAddress <= broadcastAddress
    }

    companion object {
        fun fromAnyAddress(address: Long, prefixLength: Int): IPv4Network {
            return IPv4Network(
                networkAddress = IPv4NetworkMath.networkAddressOf(address, prefixLength),
                prefixLength = prefixLength
            )
        }
    }
}
