package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.domain.OAuthService
import com.hungry.oauthsample.domain.PasswordService
import com.hungry.oauthsample.domain.UserRepository
import org.koin.dsl.module


val userKoinModule = module(createdAtStart = true) {
    single { SqlUserRepository() as UserRepository }
    single { PasswordService() }
    single { OAuthService(get(), get()) }
    single { OAuthApi(get()) }
}
