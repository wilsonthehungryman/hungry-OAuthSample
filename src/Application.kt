package com.hungry.oauthsample

import com.fasterxml.jackson.databind.SerializationFeature
import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.infrastructure.config.AppConfig
import com.hungry.oauthsample.infrastructure.userKoinModule
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Koin) {
        val appConfigModule = bindAppConfig(AppConfig())
        modules(appConfigModule, userKoinModule)
    }

    val oauthApi: OAuthApi by inject()

    routing {
        post("/user") {
            // TODO Actually Call API
            call.respond(HttpStatusCode.Created, "User Created")
        }


        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

fun bindAppConfig(appConfig: AppConfig): Module {
    return module {
        single { appConfig }
    }
}
