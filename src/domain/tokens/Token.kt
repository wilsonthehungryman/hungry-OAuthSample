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
    private val id: String?,
) {
    abstract fun expirySeconds(): Long

    fun toJwtBuilder(now: Instant): JwtBuilder {
        val expiry = now.plusSeconds(expirySeconds())
        return Jwts.builder()
            .setId(id ?: UUID.randomUUID().toString())
            .setIssuedAt(Date.from(now))
            .setIssuer(issuer)
            .setSubject(subject)
            .setAudience(audience)
            .setExpiration(Date.from(expiry))
            .setNotBefore(Date.from(now))
    }
}