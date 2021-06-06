package com.hungry.oauthsample.api.dto.out

import com.hungry.oauthsample.domain.oauth.CodeRedirect

data class CodeRedirectDto(
    val code: String,
    val state: String?,
    val uri: String,
) {
    companion object {
        fun fromDomain(codeRedirect: CodeRedirect): CodeRedirectDto {
            return CodeRedirectDto(
                codeRedirect.code,
                codeRedirect.state,
                codeRedirect.uri,
            )
        }
    }
}
