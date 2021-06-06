package com.hungry.oauthsample.domain.oauth

import java.time.Instant
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class Code(
    val code: String,
    val clientId: String,
    val userId: String,
    val expiresAt: Instant,
) {
    companion object {
        @OptIn(ExperimentalTime::class)
        val CODE_EXPIRY_SECONDS = Duration.convert(10.toDouble(), DurationUnit.MINUTES, DurationUnit.SECONDS).toLong()

        fun generate(clientId: String, userId: String, now: Instant = Instant.now()): Code {
            return Code(
                UUID.randomUUID().toString(), // TODO should be more complex
                clientId,
                userId,
                now.plusSeconds(CODE_EXPIRY_SECONDS)
            )
        }
    }
}