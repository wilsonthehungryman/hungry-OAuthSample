package com.hungry.oauthsample.domain.tokens

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import java.time.Instant
import java.util.Date
import java.util.UUID

abstract class Token (
    private val issuer: String,
    private val audience: String,
    private val subject: String,
    private val id: String = UUID.randomUUID().toString(),
    private val claims: Map<String, Any> = emptyMap(),
) {
    abstract fun expirySeconds(): Long

    fun toJwtBuilder(now: Instant): JwtBuilder {
        val expiry = now.plusSeconds(expirySeconds())
        return Jwts.builder()
            .setId(id)
            .setIssuedAt(Date.from(now))
            .setIssuer(issuer)
            .setSubject(subject)
            .setAudience(audience)
            .setExpiration(Date.from(expiry))
            .setNotBefore(Date.from(now))
            .addClaims(claims)
    }
}