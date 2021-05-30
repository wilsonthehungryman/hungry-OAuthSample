package com.hungry.oauthsample.domain

import com.hungry.oauthsample.com.hungry.oauthsample.domain.CreateUser.kt.CreateUser

class OAuthService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
) {
    fun createUser(user: CreateUser) {
        val (hashedPassword, salt) = passwordService.hashPassword(user.password)
        userRepository.save(user, hashedPassword, salt)
    }
}