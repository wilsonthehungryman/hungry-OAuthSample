package com.hungry.oauthsample.domain.oauth

import java.time.Instant

interface CodeRepository {
    fun save(code: Code)
    fun findByCode(code: String): Code?
    fun deleteCode(code: String)
    fun deleteExpiredCodes(now: Instant)
}