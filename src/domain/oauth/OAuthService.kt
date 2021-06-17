package com.hungry.oauthsample.domain.oauth

import com.hungry.oauthsample.com.hungry.oauthsample.domain.oauth.UserTokens
import com.hungry.oauthsample.domain.users.CreateUser
import com.hungry.oauthsample.domain.Forbidden
import com.hungry.oauthsample.domain.Unauthorized
import com.hungry.oauthsample.domain.users.UserRepository
import com.hungry.oauthsample.domain.client.Client
import com.hungry.oauthsample.domain.client.ClientRepository
import com.hungry.oauthsample.domain.tokens.Token
import com.hungry.oauthsample.domain.tokens.TokenService
import com.hungry.oauthsample.domain.tokens.TokenType
import java.time.Instant
import java.util.UUID

class OAuthService(
    private val passwordService: PasswordService,
    private val tokenService: TokenService,
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

        val code = Code.generate(client.id, user.id)

        codeRepository.save(code)

        return CodeRedirect(
            code.code,
            authentication.state,
            authentication.redirectUri,
            authentication.deviceId ?: UUID.randomUUID().toString(),
        )
    }

    fun exchangeCode(code: String, client: Client, deviceId: String): UserTokens {
        val now = Instant.now()
        val foundCode = codeRepository.findByCode(code) ?: throw Unauthorized()

        codeRepository.deleteCode(foundCode.code)

        if (client.id != foundCode.clientId)
            throw Unauthorized()

        val foundClient = clientRepository.findById(client.id) ?: throw Unauthorized()

        if (foundClient.secretKey != client.secretKey || foundClient.id != client.id)
            throw Unauthorized()

        val accessToken = tokenService.createAccessToken(foundCode.userId, now, client.id, deviceId)
        val refreshToken = tokenService.createRefreshToken(foundCode.userId, now, client.id, deviceId)

        return UserTokens(
            foundCode.userId,
            deviceId,
            accessToken,
            refreshToken,
        )
    }

    fun validateToken(token: String, tokenType: TokenType?): Token {
        val decoded = tokenService.decodeToken(token)
        tokenService.validateToken(token, decoded.audience, decoded.subject, tokenType, activeCheck = true)
        return decoded
    }

    fun refreshTokens(token: String): UserTokens {
        val now = Instant.now()
        val decoded = tokenService.decodeToken(token)

        val deviceId = decoded.deviceId ?: throw IllegalStateException()

        tokenService.validateToken(token, decoded.audience, decoded.subject, TokenType.REFRESH)

        val accessToken = tokenService.createAccessToken(decoded.subject, now, decoded.audience, deviceId)
        val refreshToken = tokenService.createRefreshToken(decoded.subject, now, decoded.audience, deviceId)

        return UserTokens(
            decoded.subject,
            accessToken,
            refreshToken,
        )
    }
}