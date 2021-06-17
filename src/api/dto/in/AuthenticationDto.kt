package com.hungry.oauthsample.api.dto.`in`

import com.hungry.oauthsample.domain.oauth.Authentication

data class AuthenticationDto(
    val email: String,
    val password: String,
    val clientId: String,
    val redirectUri: String,
    val state: String?,
    val scope: String?, // TODO, though not really needed atm, just for openid
    val deviceId: String?,
) {
    fun toDomain(): Authentication {
        return Authentication(
            email,
            password,
            clientId,
            redirectUri,
            state,
            scope,
            deviceId,
        )
    }
}
