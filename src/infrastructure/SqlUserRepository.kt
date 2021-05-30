package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.com.hungry.oauthsample.domain.CreateUser.kt.CreateUser
import com.hungry.oauthsample.domain.UserRepository
import com.hungry.oauthsample.infrastructure.config.AppConfig

class SqlUserRepository(
    config: AppConfig.SQLConfig
): UserRepository {
    override fun save(user: CreateUser, hashedPassword: String, salt: String) {
        TODO("Not yet implemented")
    }
}