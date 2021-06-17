package com.hungry.oauthsample.domain.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator.Builder
import com.auth0.jwt.interfaces.DecodedJWT
import com.hungry.oauthsample.domain.Unauthorized
import java.time.Instant
import java.util.Date
import java.util.UUID

fun Builder.withClaim(name: String, value: Instant): Builder {
    this.withClaim(name, Date.from(value))
    return this
}

fun Builder.withClaims(claims: Map<String, Any>): Builder {
    claims.entries.forEach {
        when (val value = it.value) {
            is String -> this.withClaim(it.key, value)
            is Boolean -> this.withClaim(it.key, value)
            is Instant -> this.withClaim(it.key, value)
            is Date -> this.withClaim(it.key, value)
            is Int -> this.withClaim(it.key, value)
            is Double -> this.withClaim(it.key, value)
            is Long -> this.withClaim(it.key, value)
            is List<*> -> this.withClaim(it.key, value)
            is Enum<*> -> this.withClaim(it.key, value.toString())
            // TODO arrays and map, not needed for my use case
//            is Map<*, *> -> this.withClaim(it.key, value as Map<String, *>)
        }
    }
    return this
}

class Token (
    val issuer: String,
    val audience: String,
    val subject: String,
    val type: TokenType,
    val issuedAt: Instant,
    val expiresAt: Instant,
    val deviceId: String? = null,
    val id: String = UUID.randomUUID().toString(),
    val claims: Map<String, Any> = emptyMap(),
) {
    constructor(jwt: DecodedJWT) : this(
        jwt.issuer,
        jwt.audience.first(),
        jwt.subject,
        TokenType.valueOf(jwt.claims[TOKEN_TYPE]?.asString() ?: throw Unauthorized()),
        jwt.issuedAt.toInstant(),
        jwt.expiresAt.toInstant(),
        jwt.claims[DEVICE_ID]?.asString(),
        jwt.id,
        jwt.claims,
    )

    companion object {
        const val TOKEN_TYPE = "tokenType"
        const val DEVICE_ID = "deviceId"
    }

    fun isExpired(now: Instant): Boolean {
        return expiresAt < now
    }

    fun toJwtBuilder(): Builder {
        val claimsToAdd = claims.toMutableMap()
        claimsToAdd[TOKEN_TYPE] = type
        deviceId?.also { claimsToAdd[DEVICE_ID] = it }

        return JWT.create()
            .withJWTId(id)
            .withIssuedAt(Date.from(issuedAt))
            .withIssuer(issuer)
            .withSubject(subject)
            .withAudience(audience)
            .withExpiresAt(Date.from(expiresAt))
            .withNotBefore(Date.from(issuedAt))
            .withClaims(claimsToAdd)
    }
}
