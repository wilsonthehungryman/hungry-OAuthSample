package com.hungry.oauthsample.api

import com.hungry.oauthsample.api.dto.`in`.CreateUserDto
import com.hungry.oauthsample.api.dto.ClientDto
import com.hungry.oauthsample.domain.OAuthService

class OAuthApi(
    private val service: OAuthService,
) {
    fun createUser(userDto: CreateUserDto) {
        service.createUser(userDto.toDomain())
    }

    fun createClient(): ClientDto {
        return ClientDto.fromDomain(service.createClient())
    }

    fun updateRedirects(clientDto: ClientDto) {
        service.updateRedirects(clientDto.toDomain())
    }
}