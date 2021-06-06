package com.hungry.oauthsample.domain.oauth

import com.hungry.oauthsample.domain.users.CreateUser
import com.hungry.oauthsample.domain.Forbidden
import com.hungry.oauthsample.domain.Unauthorized
import com.hungry.oauthsample.domain.users.UserRepository
import com.hungry.oauthsample.domain.client.Client
import com.hungry.oauthsample.domain.client.ClientRepository

class OAuthService(
    private val passwordService: PasswordService,
    private val userRepository: UserRepository,
    private val clientRepository: ClientRepository,
    private val codeRepository: CodeRepository,
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

    fun updateRedirects(client: Client) {
        val foundClient = clientRepository.findById(client.id) ?: throw Forbidden()

        if (client.secretKey == foundClient.secretKey) {
            clientRepository.updateRedirects(client)
        } else {
            throw Forbidden()
        }
    }

    fun authenticateCode(authentication: Authentication): CodeRedirect {
        val client = clientRepository.findById(authentication.clientId) ?: throw Unauthorized()

        if (!client.redirectUris.contains(authentication.redirectUri))
            throw Unauthorized()

        val user = userRepository.findByEmail(authentication.email) ?: throw Unauthorized()
        val credential = userRepository.findCredentialById(user.id) ?: throw Unauthorized()

        if (!passwordService.isMatchingPassword(authentication.password, credential.hashedPassword))
            throw Unauthorized()

        val code = Code.generate(client.id)

        codeRepository.save(code)

        return CodeRedirect(code.code, authentication.state, authentication.redirectUri)
    }
}