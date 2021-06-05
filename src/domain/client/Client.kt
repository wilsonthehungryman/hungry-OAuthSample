package com.hungry.oauthsample.domain.client

import java.util.UUID

class Client (
    val id: String,
    val secretKey: String,
    val redirectUris: Set<String> = emptySet()
        ) {
    companion object {
        fun generate(): Client {
            return Client(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), // TODO should be more complex
            )
        }
    }
}