package com.hungry.oauthsample.domain.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator.Builder
import com.auth0.jwt.exceptions.JWTVerificationException
import com.hungry.oauthsample.domain.Unauthorized
import com.hungry.oauthsample.infrastructure.config.AppConfig
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class TokenService(
    private val tokenRepository: TokenRepository,
    private val keys: AppConfig.RSAKeyConfig
) {
    companion object {
        val ACCESS_TOKEN_EXPIRY_SECONDS = Duration.convert(1.toDouble(), DurationUnit.HOURS, DurationUnit.SECONDS).toLong()
        val REFRESH_TOKEN_EXPIRY_SECONDS = Duration.convert(30.toDouble(), DurationUnit.DAYS, DurationUnit.SECONDS).toLong()
        val ID_TOKEN_EXPIRY_SECONDS = Duration.convert(1.toDouble(), DurationUnit.DAYS, DurationUnit.SECONDS).toLong()
        const val ISSUER = "hungry"
    }

    private fun sign(builder: Builder): String {
        return builder.sign(keys.algorithm)
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

        return sign(token.toJwtBuilder())
    }

    fun createRefreshToken(userId: String, now: Instant, audience: String): String {
        val token = Token(
            ISSUER,
            audience,
            userId,
            now,
            now.plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS),
        )

        return sign(token.toJwtBuilder())
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

        return sign(token.toJwtBuilder())
    }

    fun validateToken(jwtString: String, audience: String, userId: String): Token {
        val verifier = JWT.require(keys.algorithm)
            .withIssuer(ISSUER)
            .withAudience(audience)
            .withSubject(userId)
            .build()

        try {
            val decoded = verifier.verify(jwtString)
            return Token(decoded)
        } catch (e: JWTVerificationException) {
            throw Unauthorized()
        }
    }
}
