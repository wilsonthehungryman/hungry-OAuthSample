package com.hungry.oauthsample.domain.tokens

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import java.time.Instant
import java.util.Date
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class Token (
    val issuer: String,
    val audience: String,
    val subject: String,
    val issuedAt: Instant,
    val expiresAt: Instant,
    val id: String = UUID.randomUUID().toString(),
    val claims: Map<String, Any> = emptyMap(),
) {
    constructor(claims: Jws<Claims>) : this(
        claims.body.issuer,
        claims.body.audience,
        claims.body.subject,
        claims.body.issuedAt.toInstant(),
        claims.body.expiration.toInstant(),
        claims.body.id,
        claims.body.toMap(),
    )

    fun isExpired(now: Instant): Boolean {
        return expiresAt < now
    }

    fun toJwtBuilder(): JwtBuilder {
        return Jwts.builder()
            .setId(id)
            .setIssuedAt(Date.from(issuedAt))
            .setIssuer(issuer)
            .setSubject(subject)
            .setAudience(audience)
            .setExpiration(Date.from(expiresAt))
            .setNotBefore(Date.from(issuedAt))
            .addClaims(claims)
    }
}
