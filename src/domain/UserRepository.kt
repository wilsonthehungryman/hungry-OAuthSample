package com.hungry.oauthsample.domain

import com.hungry.oauthsample.com.hungry.oauthsample.domain.CreateUser.kt.CreateUser

interface UserRepository {
    fun save(user: CreateUser, hashedPassword: String, salt: String)
}