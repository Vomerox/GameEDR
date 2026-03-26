package fr.gir.gameedr.domain.service

object IPv4Parser {
    fun parseIpToLong(rawValue: String): Long? {
        val sanitized = rawValue.trim()
        val octets = sanitized.split(".")
        if (octets.size != 4) {
            return null
        }

        var result = 0L
        for (octet in octets) {
            if (octet.isBlank() || octet.length > 3) {
                return null
            }

            val value = octet.toIntOrNull() ?: return null
            if (value !in 0..255) {
                return null
            }

            result = (result shl 8) or value.toLong()
        }
        return result
    }

    fun parseMaskOrCidrToPrefix(rawValue: String): Int? {
        val sanitized = rawValue.trim()
        if (sanitized.isEmpty()) {
            return null
        }

        if (sanitized.startsWith("/")) {
            val prefix = sanitized.removePrefix("/").toIntOrNull() ?: return null
            return prefix.takeIf { it in 0..32 }
        }

        sanitized.toIntOrNull()?.let { prefix ->
            return prefix.takeIf { it in 0..32 }
        }

        val maskValue = parseIpToLong(sanitized) ?: return null
        return IPv4NetworkMath.maskToPrefix(maskValue)
    }
}
