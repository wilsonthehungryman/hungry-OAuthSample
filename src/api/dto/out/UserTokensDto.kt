package com.hungry.oauthsample.api.dto.out

import com.hungry.oauthsample.com.hungry.oauthsample.domain.oauth.UserTokens

data class UserTokensDto(
    val userId: String,
    val accessToken: String,
    val refreshToken: String? = null,
    val idToken: String? = null,
) {
    companion object {
        fun fromDomain(userTokens: UserTokens): UserTokensDto {
            return UserTokensDto(
                userTokens.userId,
                userTokens.accessToken,
                userTokens.refreshToken,
                userTokens.idToken,
            )
        }
    }
}