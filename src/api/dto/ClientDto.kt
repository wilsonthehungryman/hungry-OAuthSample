package com.hungry.oauthsample.api.dto

import com.hungry.oauthsample.domain.client.Client

data class ClientDto(
    val id: String,
    val secretKey: String,
    val redirectUris: Set<String> = emptySet(),
) {
    companion object {
        fun fromDomain(domain: Client): ClientDto {
            return ClientDto(
                domain.id,
                domain.secretKey,
                domain.redirectUris,
            )
        }
    }

    fun toDomain(): Client {
        return Client(
            id,
            secretKey,
            redirectUris,
        )
    }
}
