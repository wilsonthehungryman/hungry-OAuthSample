package com.hungry.oauthsample.domain.oauth

class CodeRedirect(
    val code: String,
    val state: String?,
    val uri: String,
    val deviceId: String,
) {
}