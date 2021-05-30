package com.hungry.oauthsample

import com.fasterxml.jackson.databind.SerializationFeature
import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.infrastructure.config.AppConfig
import com.hungry.oauthsample.infrastructure.oauthRoutes
import com.hungry.oauthsample.infrastructure.userKoinModule
import com.viartemev.ktor.flyway.FlywayFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val appConfig = AppConfig()

    install(Authentication) {
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    appConfig.sqlConfig.connect()
    install(FlywayFeature) {
        dataSource = appConfig.sqlConfig.dataSource
    }

    install(Koin) {
        val appConfigModule = bindAppConfig(appConfig)
        modules(appConfigModule, userKoinModule)
    }

    val oauthApi: OAuthApi by inject()

    routing {
        oauthRoutes(oauthApi)
    }
}

fun bindAppConfig(appConfig: AppConfig): Module {
    return module {
        single { appConfig }
    }
}
