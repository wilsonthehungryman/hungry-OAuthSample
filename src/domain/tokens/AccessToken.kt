package com.hungry.oauthsample.domain.tokens

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class AccessToken(
    issuer: String,
    audience: String,
    subject: String,
) : Token(
    issuer,
    audience,
    subject,
) {
    @OptIn(ExperimentalTime::class)
    override fun expirySeconds(): Long {
        return Duration.convert(1.toDouble(), DurationUnit.HOURS, DurationUnit.SECONDS).toLong()
    }
}
