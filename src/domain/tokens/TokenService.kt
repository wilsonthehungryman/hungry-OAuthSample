package com.hungry.oauthsample.domain.tokens

import com.hungry.oauthsample.domain.Unauthorized
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class TokenService(
    private val tokenRepository: TokenRepository
) {
    companion object {
        val ACCESS_TOKEN_EXPIRY_SECONDS = Duration.convert(1.toDouble(), DurationUnit.HOURS, DurationUnit.SECONDS).toLong()
        val REFRESH_TOKEN_EXPIRY_SECONDS = Duration.convert(30.toDouble(), DurationUnit.DAYS, DurationUnit.SECONDS).toLong()
        val ID_TOKEN_EXPIRY_SECONDS = Duration.convert(1.toDouble(), DurationUnit.DAYS, DurationUnit.SECONDS).toLong()
        val SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256
        const val ISSUER = "hungry"
        const val SIGNING_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJeYMv2SpAiG0Us7ItHHthQRbPDPXupNrhhNIDkVqgaODSKxDYXLH3bUS8CsrHYxR5iVVAN6UH+cO9wqWMk0j4I9TB7yA3gxXpakzKSvNNj0pGbrvJTZUx4MFW3zNSJvxfgdIpnhxciAXV/eFOaGJ+Ep5NIg7pThQ1mpTIQ9sMhdAgMBAAECgYBY94vFYZA/KNAf9L7Emw5yzJ9A3JL5s0kfHfRxTBOS8T8oQvyKruDyXjlvBGfanaVFGS5LNDeWDNzco0WkH+KwR3nXGvcJNKbHX/iClswaIL+vGv2qnZLmFlXfgNqKHDQR5Z6YmOwiX1NWNlt8epQA09uFv0AkZEtAd2CqPHZPSQJBAMm0/EufMJrQgNZewgutQDa90bqXs9l2GhX5Fs0rOJxEhhTMkuAlI/dP0i0xmlBebTtBuAMFhk2HLvvct+rmxkcCQQDAZh4GgJRYpEoGEtC2XIAEG83y/GZ0qic3WjA05PrcQ33Qy7dFiiaAoORaY7mKMbBDKjuvJK7cr7A+iqMrQzo7AkBExC4rfectMJ5r9zxCjNtSjl5dNfD713bHPPeYFP8kz3vCxYfhYOglHZJzE/EfK0IkLKCWbyC9d7P9697L98drAkEAkoWyXBVT5GXJkfKGTDsJqxdZsrERfcuPbhIbIE051sD3cimSez0IsXDWNxg3Vs0chSoGpg1ztdF+kUKXnPznvQJAAUNgyHNwqZqVUJ/8PAFBecYlK2EtFwTJheTkg/Pbkw3aC7oMbWe5ZaSd5jNRrUXykBSk4G/91oGAn9I0Rb7xkA=="
    }

    private fun sign(builder: JwtBuilder) {
        builder.signWith(SIGNATURE_ALGORITHM, SIGNING_KEY)
    }

    fun createAccessToken(userId: String, now: Instant, audience: String): String {
        val token = Token(
            ISSUER,
            audience,
            userId,
            now,
            now.plusSeconds(ACCESS_TOKEN_EXPIRY_SECONDS),
        )

        tokenRepository.save(token)

        return signToken(token.toJwtBuilder())
    }

    fun createRefreshToken(userId: String, now: Instant, audience: String, tokenId: String): String {
        val token = Token(
            ISSUER,
            audience,
            userId,
            now,
            now.plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS),
            tokenId,
        )

        return signToken(token.toJwtBuilder())
    }

    fun createIdToken(userId: String, now: Instant, audience: String, claims: Map<String, Any>): String {
        val token = Token(
            ISSUER,
            audience,
            userId,
            now,
            now.plusSeconds(ID_TOKEN_EXPIRY_SECONDS),
            claims = claims,
        )

        return signToken(token.toJwtBuilder())
    }

    private fun signToken(builder: JwtBuilder): String {
        builder.signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
        sign(builder)

        return builder.compact()
    }

    fun validateToken(jwtString: String): Token {
        // TODO, need to unit test this and/or check lib, does it validate expiration?
        val token = Token(Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(jwtString))

        val foundToken = tokenRepository.findById(token.id) ?: throw Unauthorized()

        TODO()
    }
}
