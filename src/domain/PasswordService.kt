package com.hungry.oauthsample.domain

import java.util.UUID

class PasswordService {
    val pepper = "replace me"

    fun hashPassword(password: String): Pair<String, String> {
        val salt = UUID.randomUUID().toString()
        // TODO actually hash
        return Pair("todo", salt)
    }
}