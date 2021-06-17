package com.hungry.oauthsample.domain.tokens

import java.time.Instant

interface RevokedTokenCache {
    fun addRevokedToken(id: String, expiration: Instant)
    fun addRevokedToken(token: Token) {
        addRevokedToken(token.id, token.expiresAt)
    }
    fun addRevokedTokens(tokens: Set<Token>)
    fun isRevokedToken(id: String): Boolean
    fun isRevokedToken(token: Token): Boolean {
        return isRevokedToken(token.id)
    }
    fun removeExpiredTokens(now: Instant)
}