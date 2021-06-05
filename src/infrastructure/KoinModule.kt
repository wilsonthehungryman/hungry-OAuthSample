package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.domain.oauth.OAuthService
import com.hungry.oauthsample.domain.oauth.PasswordService
import com.hungry.oauthsample.domain.users.UserRepository
import com.hungry.oauthsample.domain.client.ClientRepository
import com.hungry.oauthsample.domain.tokens.TokenRepository
import com.hungry.oauthsample.domain.tokens.TokenService
import org.koin.dsl.module


val userKoinModule = module(createdAtStart = true) {
    single { SqlUserRepository() as UserRepository }
    single { SqlTokenRepository() as TokenRepository }
    single { SqlClientRepository() as ClientRepository }
    
    single { PasswordService() }
    single { TokenService(get()) }
    single { OAuthService(get(), get(), get()) }

    single { OAuthApi(get()) }
}
