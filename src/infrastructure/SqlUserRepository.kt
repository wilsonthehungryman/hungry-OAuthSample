package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.com.hungry.oauthsample.domain.CreateUser.kt.CreateUser
import com.hungry.oauthsample.domain.UserRepository

class SqlUserRepository: UserRepository {
    override fun save(user: CreateUser, hashedPassword: String, salt: String) {
        TODO("Not yet implemented")
    }
}