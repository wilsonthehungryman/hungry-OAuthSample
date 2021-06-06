package com.hungry.oauthsample.api

import com.hungry.oauthsample.api.dto.ClientDto
import com.hungry.oauthsample.api.dto.`in`.AuthenticationDto
import com.hungry.oauthsample.api.dto.`in`.CreateUserDto
import com.hungry.oauthsample.api.dto.out.CodeRedirectDto
import com.hungry.oauthsample.domain.oauth.OAuthService

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

    fun authenticate(authenticationDto: AuthenticationDto): CodeRedirectDto {
        return CodeRedirectDto.fromDomain(service.authenticateCode(authenticationDto.toDomain()))
    }
}