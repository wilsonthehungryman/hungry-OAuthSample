package com.hungry.oauthsample.com.hungry.oauthsample.domain.oauth

class UserTokens(
    val userId: String,
    val accessToken: String,
    val refreshToken: String? = null,
    val idToken: String? = null,
) {

}
