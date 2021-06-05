package com.hungry.oauthsample.domain

import com.hungry.oauthsample.domain.client.Client
import com.hungry.oauthsample.domain.client.ClientRepository

class OAuthService(
    private val passwordService: PasswordService,
    private val userRepository: UserRepository,
    private val clientRepository: ClientRepository,
) {
    fun createUser(createUser: CreateUser) {
        val hashedPassword = passwordService.hashPassword(createUser.password)
        userRepository.saveUser(createUser.toUser(), hashedPassword)
    }

    fun createClient(): Client {
        val client = Client.generate()
        clientRepository.save(client)
        return client
    }
}