package com.hungry.oauthsample.domain.users

import java.util.UUID

class CreateUser(
    val email: String,
    val password: String,
    val phoneNumber: String,
    val name: String?,
    val age: Int?,
) {
    fun toUser(): User {
        return User(
            UUID.randomUUID().toString(),
            email,
            phoneNumber,
            name,
            age,
        )
    }
}
