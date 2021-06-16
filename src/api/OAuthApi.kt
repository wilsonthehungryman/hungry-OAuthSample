package com.hungry.oauthsample.api

import com.hungry.oauthsample.api.dto.ClientDto
import com.hungry.oauthsample.api.dto.`in`.AuthenticationDto
import com.hungry.oauthsample.api.dto.`in`.CodeExchangeDto
import com.hungry.oauthsample.api.dto.`in`.CreateUserDto
import com.hungry.oauthsample.api.dto.out.CodeRedirectDto
import com.hungry.oauthsample.api.dto.out.UserTokensDto
import com.hungry.oauthsample.domain.client.Client
import com.hungry.oauthsample.domain.oauth.OAuthService
import com.hungry.oauthsample.domain.tokens.Token
import com.hungry.oauthsample.domain.tokens.TokenType

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

    fun exchangeCode(codeExchangeDto: CodeExchangeDto): UserTokensDto {
        val client = Client(codeExchangeDto.clientId, codeExchangeDto.clientSecret)
        return UserTokensDto.fromDomain(service.exchangeCode(codeExchangeDto.code, client))
    }

    fun validateToken(token: String, tokenType: TokenType? = null): Token {
        return service.validateToken(token, tokenType)
    }

    fun refreshTokens(token: String): UserTokensDto {
        return UserTokensDto.fromDomain(service.refreshTokens(token))
    }
}