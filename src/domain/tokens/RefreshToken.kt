package com.hungry.oauthsample.domain.tokens

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class RefreshToken(
    issuer: String,
    audience: String,
    subject: String,
    id: String,
) : Token(
    issuer,
    audience,
    subject,
    id,
) {
    @OptIn(ExperimentalTime::class)
    override fun expirySeconds(): Long {
        return Duration.convert(30.toDouble(), DurationUnit.DAYS, DurationUnit.SECONDS).toLong()
    }
}