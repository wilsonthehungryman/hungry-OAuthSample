package com.hungry.oauthsample.domain

import com.hungry.oauthsample.com.hungry.oauthsample.domain.CreateUser.kt.CreateUser

class OAuthService(
    val userRepository: UserRepository,
    val passwordService: passwordService,
) {
    fun createUser(user: CreateUser) {
        val (hashedPassword, salt) = passwordService.hashPassword(user.password)
        userRepository.save(user, hashedPassword, salt)
    }
}