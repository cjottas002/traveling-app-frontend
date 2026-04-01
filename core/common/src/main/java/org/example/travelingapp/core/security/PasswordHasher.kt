package org.example.travelingapp.core.security

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * PBKDF2 password hashing for offline credential caching.
 * Uses PBKDF2WithHmacSHA256 with 10,000 iterations.
 * Optimized for mobile devices — this is local cache only, not a server-side store.
 * Format: "{iterations}:{salt_base64}:{hash_base64}"
 */
object PasswordHasher {

    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val ITERATIONS = 10_000
    private const val KEY_LENGTH = 256
    private const val SALT_LENGTH = 16

    fun hash(password: String): String {
        val salt = ByteArray(SALT_LENGTH).also { SecureRandom().nextBytes(it) }
        val hash = pbkdf2(password, salt)
        val encoder = Base64.getEncoder()
        val saltBase64 = encoder.encodeToString(salt)
        val hashBase64 = encoder.encodeToString(hash)
        return "$ITERATIONS:$saltBase64:$hashBase64"
    }

    fun verify(password: String, stored: String): Boolean {
        val parts = stored.split(":")
        if (parts.size != 3) return false
        val iterations = parts[0].toIntOrNull() ?: return false
        val decoder = Base64.getDecoder()
        val salt = decoder.decode(parts[1])
        val expectedHash = decoder.decode(parts[2])
        val actualHash = pbkdf2(password, salt, iterations)
        return actualHash.contentEquals(expectedHash)
    }

    private fun pbkdf2(password: String, salt: ByteArray, iterations: Int = ITERATIONS): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        return factory.generateSecret(spec).encoded
    }
}
