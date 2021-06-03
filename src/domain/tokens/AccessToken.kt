package com.hungry.oauthsample.domain.tokens

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class AccessToken(
    issuer: String,
    audience: String,
    subject: String,
    id: String? = null,
) : Token(
    issuer,
    audience,
    subject,
    id,
) {
    @OptIn(ExperimentalTime::class)
    override fun expirySeconds(): Long {
        // 1 hour
        return Duration.convert(1.toDouble(), DurationUnit.HOURS, DurationUnit.SECONDS).toLong()
    }
}
