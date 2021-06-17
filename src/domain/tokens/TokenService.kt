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
    private val revokedTokenCache: RevokedTokenCache,
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

    fun createAccessToken(userId: String, now: Instant, audience: String, deviceId: String): String {
        val token = Token(
            ISSUER,
            audience,
            userId,
            TokenType.ACCESS,
            now,
            now.plusSeconds(ACCESS_TOKEN_EXPIRY_SECONDS),
            deviceId,
        )

        tokenRepository.save(token)

        return sign(token.toJwtBuilder())
    }

    fun createRefreshToken(userId: String, now: Instant, audience: String, deviceId: String): String {
        val token = Token(
            ISSUER,
            audience,
            userId,
            TokenType.REFRESH,
            now,
            now.plusSeconds(REFRESH_TOKEN_EXPIRY_SECONDS),
            deviceId,
        )

        tokenRepository.save(token)

        return sign(token.toJwtBuilder())
    }

    fun createIdToken(userId: String, now: Instant, audience: String, deviceId: String, claims: Map<String, Any>): String {
        val token = Token(
            ISSUER,
            audience,
            userId,
            TokenType.ID,
            now,
            now.plusSeconds(ID_TOKEN_EXPIRY_SECONDS),
            deviceId,
            claims = claims,
        )

        tokenRepository.save(token)

        return sign(token.toJwtBuilder())
    }

    fun decodeToken(jwtString: String): Token {
        return Token(JWT.decode(jwtString))
    }

    fun validateToken(jwtString: String, audience: String, userId: String, tokenType: TokenType?, activeCheck: Boolean = false): Token {
        val verifier = JWT.require(keys.algorithm)
            .withIssuer(ISSUER)
            .withAudience(audience)
            .withSubject(userId)

        tokenType?.also { verifier.withClaim(Token.TOKEN_TYPE, tokenType.toString()) }

        try {
            val decoded = Token(verifier.build().verify(jwtString))

            if (revokedTokenCache.isRevokedToken(decoded.id))
                throw Unauthorized()

            if (activeCheck || tokenType == TokenType.REFRESH)
                tokenActiveCheck(decoded)

            return decoded
        } catch (e: JWTVerificationException) {
            throw Unauthorized()
        }
    }

    private fun tokenActiveCheck(token: Token) {
        tokenRepository.findById(token.id) ?: throw Unauthorized()
    }

    fun logoutEverywhere(userId: String) {
        tokenRepository.deleteByUserId(userId)
    }

    fun logoutDevice(deviceId: String) {
        tokenRepository.deleteByDeviceId(deviceId)
    }
}
