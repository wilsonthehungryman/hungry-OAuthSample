package com.hungry.oauthsample.api.dto.out

data class UserTokensDto(
    val userId: String,
    val accessToken: String,
    val refreshToken: String? = null,
    val idToken: String? = null,
) {
}