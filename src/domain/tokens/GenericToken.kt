package com.hungry.oauthsample.domain.tokens

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws

class GenericToken(private val claims: Jws<Claims>): Token(
    claims.body.issuer,
    claims.body.audience,
    claims.body.subject,
    claims.body.id,
    claims.body.toMap(),
) {
    override fun expirySeconds(): Long {
        return claims.body.expiration.toInstant().epochSecond - claims.body.issuedAt.toInstant().epochSecond
    }
}