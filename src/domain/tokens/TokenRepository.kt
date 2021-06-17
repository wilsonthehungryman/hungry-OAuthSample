package com.hungry.oauthsample.domain.tokens

import java.time.Instant

interface TokenRepository {
    fun save(token: Token)
    fun findById(id: String): Token?
    fun findByUserId(userId: String): Set<Token>
    fun findByDeviceId(deviceId: String): Set<Token>
    fun deleteById(id: String)
    fun deleteByUserId(userId: String)
    fun deleteByDeviceId(deviceId: String)
    fun deleteExpiredTokens(now: Instant)
}