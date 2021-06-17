package com.hungry.oauthsample.api.dto.out

import com.hungry.oauthsample.domain.oauth.CodeRedirect
import io.ktor.http.URLBuilder

data class CodeRedirectDto(
    val code: String,
    val state: String?,
    val uri: String,
    val deviceId: String,
) {
    fun toUrl(): URLBuilder {
        val builder = URLBuilder(uri)
        builder.parameters.append("code", code)
        builder.parameters.append("deviceId", deviceId)
        state?.also { builder.parameters.append("state", state) }
        return builder
    }

    companion object {
        fun fromDomain(codeRedirect: CodeRedirect): CodeRedirectDto {
            return CodeRedirectDto(
                codeRedirect.code,
                codeRedirect.state,
                codeRedirect.uri,
                codeRedirect.deviceId,
            )
        }
    }
}
