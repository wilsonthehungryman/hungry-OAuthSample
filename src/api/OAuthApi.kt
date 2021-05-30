package com.hungry.oauthsample.api

import com.hungry.oauthsample.api.dto.`in`.CreateUserDto
import com.hungry.oauthsample.domain.OAuthService

class OAuthApi(val service: OAuthService) {
    fun createUser(userDto: CreateUserDto) {
        service.createUser(userDto.toDomain())
    }
}