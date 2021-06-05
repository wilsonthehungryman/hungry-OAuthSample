package com.hungry.oauthsample.domain.oauth

class Authentication(
    val email: String,
    val password: String,
    val clientId: String,
    val redirectUri: String,
    val scope: String?
) {
}