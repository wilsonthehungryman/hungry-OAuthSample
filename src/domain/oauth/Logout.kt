package com.hungry.oauthsample.domain.oauth

class Logout(
    val logoutEverywhere: Boolean,
    val userId: String,
    val deviceId: String,
) {
}