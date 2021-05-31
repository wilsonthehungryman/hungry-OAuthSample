package com.hungry.oauthsample.domain

interface UserRepository {
    fun saveUser(user: User, hashedPassword: String, salt: String)
}