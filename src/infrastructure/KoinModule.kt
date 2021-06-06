package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.domain.oauth.OAuthService
import com.hungry.oauthsample.domain.oauth.PasswordService
import com.hungry.oauthsample.domain.users.UserRepository
import com.hungry.oauthsample.domain.client.ClientRepository
import com.hungry.oauthsample.domain.oauth.CodeRepository
import com.hungry.oauthsample.domain.tokens.TokenRepository
import com.hungry.oauthsample.domain.tokens.TokenService
import com.hungry.oauthsample.infrastructure.sql.SqlClientRepository
import com.hungry.oauthsample.infrastructure.sql.SqlCodeRepository
import com.hungry.oauthsample.infrastructure.sql.SqlTokenRepository
import com.hungry.oauthsample.infrastructure.sql.SqlUserRepository
import org.koin.dsl.module


val userKoinModule = module(createdAtStart = true) {
    single { SqlUserRepository() as UserRepository }
    single { SqlTokenRepository() as TokenRepository }
    single { SqlClientRepository() as ClientRepository }
    single { SqlCodeRepository() as CodeRepository }
    
    single { PasswordService() }
    single { TokenService(get()) }
    single { OAuthService(get(), get(), get(), get(), get()) }

    single { OAuthApi(get()) }
}
