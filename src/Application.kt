package com.hungry.oauthsample

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.SerializationFeature
import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.domain.Forbidden
import com.hungry.oauthsample.domain.Unauthorized
import com.hungry.oauthsample.infrastructure.config.AppConfig
import com.hungry.oauthsample.infrastructure.oauthRoutes
import com.hungry.oauthsample.infrastructure.userKoinModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import org.jetbrains.exposed.sql.Database
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import kotlin.time.ExperimentalTime

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val appConfig = AppConfig()

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(StatusPages) {
        exception<Unauthorized> { this.context.respond(HttpStatusCode.Unauthorized) }
        exception<Forbidden> { this.context.respond(HttpStatusCode.Forbidden) }
        exception<JsonParseException> { this.context.respond(HttpStatusCode.BadRequest) }
    }

    val db = appConfig.sqlConfig.dataSource
    Database.connect(db)
    // Actually, might be fine without
//    install(FlywayFeature) {
//        dataSource = db
//        schemas = arrayOf("users")
//    }

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
