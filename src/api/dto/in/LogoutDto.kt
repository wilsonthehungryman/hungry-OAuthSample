package com.hungry.oauthsample.api.dto.`in`

import com.hungry.oauthsample.domain.oauth.Logout
import com.hungry.oauthsample.domain.tokens.Token

data class LogoutDto(
    val logoutEverywhere: Boolean,
) {
    fun toDomain(token: Token): Logout {
        return Logout(
            logoutEverywhere,
            token.subject,
            token.deviceId,
        )
    }
}
