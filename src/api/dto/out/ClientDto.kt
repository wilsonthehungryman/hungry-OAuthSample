package com.hungry.oauthsample.api.dto.out

import com.hungry.oauthsample.domain.client.Client

data class ClientDto(
    val id: String,
    val secretKey: String,
) {
    companion object {
        fun fromDomain(domain: Client): ClientDto {
            return ClientDto(
                domain.id,
                domain.secretKey
            )
        }
    }
}
