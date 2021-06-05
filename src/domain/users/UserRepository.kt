package com.hungry.oauthsample.domain.users

import com.hungry.oauthsample.domain.oauth.Credential

interface UserRepository {
    fun saveUser(user: User, hashedPassword: String)
    fun findByEmail(email: String): User?
    fun findCredentialById(userId: String): Credential?
}