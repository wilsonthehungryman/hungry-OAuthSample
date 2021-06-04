package com.hungry.oauthsample.domain.tokens

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class IdToken(
    issuer: String,
    audience: String,
    subject: String,
    claims: Map<String, Any>,
) : Token(
    issuer,
    audience,
    subject,
    claims = claims,
) {
    @OptIn(ExperimentalTime::class)
    override fun expirySeconds(): Long {
        return Duration.convert(1.toDouble(), DurationUnit.DAYS, DurationUnit.SECONDS).toLong()
    }
}