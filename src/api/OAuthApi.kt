package com.hungry.oauthsample.api

import com.hungry.oauthsample.api.dto.`in`.CreateUserDto
import com.hungry.oauthsample.api.dto.out.ClientDto
import com.hungry.oauthsample.domain.OAuthService
import java.util.UUID

class OAuthApi(
    private val service: OAuthService,
) {
    fun createUser(userDto: CreateUserDto) {
        service.createUser(userDto.toDomain())
    }

    fun createClient(): ClientDto {
        return ClientDto.fromDomain(service.createClient())
    }
}