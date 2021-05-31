package com.hungry.oauthsample.domain

class OAuthService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
) {
    fun createUser(createUser: CreateUser) {
        val hashedPassword = passwordService.hashPassword(createUser.password)
        userRepository.saveUser(createUser.toUser(), hashedPassword)
    }
}