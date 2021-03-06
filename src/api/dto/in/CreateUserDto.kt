package com.hungry.oauthsample.api.dto.`in`

import com.hungry.oauthsample.domain.users.CreateUser


data class CreateUserDto(
    val email: String,
    val password: String,
    val phoneNumber: String,
    val name: String?,
    val age: Int?,
) {
    fun toDomain() = CreateUser(
        email,
        password,
        phoneNumber,
        name,
        age,
    )
}
