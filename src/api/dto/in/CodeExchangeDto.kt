package com.hungry.oauthsample.api.dto.`in`

data class CodeExchangeDto(
    val code: String,
    val clientId: String,
    val clientSecret: String,
    ) {
}