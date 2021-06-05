package com.hungry.oauthsample.domain.users

interface UserRepository {
    fun saveUser(user: User, hashedPassword: String)
}