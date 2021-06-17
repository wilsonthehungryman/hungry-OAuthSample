package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.api.dto.ClientDto
import com.hungry.oauthsample.api.dto.`in`.AuthenticationDto
import com.hungry.oauthsample.api.dto.`in`.CodeExchangeDto
import com.hungry.oauthsample.api.dto.`in`.CreateUserDto
import com.hungry.oauthsample.api.dto.`in`.ValidateToken
import com.hungry.oauthsample.domain.Unauthorized
import com.hungry.oauthsample.domain.tokens.Token
import com.hungry.oauthsample.domain.tokens.TokenType
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route

fun Route.oauthRoutes(oAuthApi: OAuthApi) {
    fun isAuthenticated(call: ApplicationCall, tokenType: TokenType? = null): Token {
        val rawToken = call.request.headers["Authorization"]?.replace("Bearer", "")?.trim() ?: throw Unauthorized()
        return oAuthApi.validateToken(rawToken, tokenType)
    }

    route("/oauth") {
        post ("/authentication") {
            val codeRedirect = oAuthApi.authenticate(call.receive<AuthenticationDto>())
            call.respondRedirect(codeRedirect.toUrl().buildString(), false)
        }

        post ("/exchange/code") {
            call.respond(HttpStatusCode.OK, oAuthApi.exchangeCode(call.receive<CodeExchangeDto>()))
        }

        route("/validate") {
            get {
                isAuthenticated(call)
                call.respond(HttpStatusCode.NoContent)
            }

            post {
                oAuthApi.validateToken(call.receive<ValidateToken>().token)
                call.respond(HttpStatusCode.NoContent)
            }
        }

        post ("/refresh") {
            call.respond(HttpStatusCode.OK, oAuthApi.refreshTokens(call.receive<ValidateToken>().token))
        }
    }

    route("/user") {
        post {
            oAuthApi.createUser(call.receive<CreateUserDto>())
            call.respond(HttpStatusCode.Created, "User Created")
        }
    }

    route("/client") {
        // TODO should be more along the lines of, create user, register client to user, client created, only created user can manage
        get {
            call.respond(HttpStatusCode.Created, oAuthApi.createClient())
        }

        // TODO as said above, should be authorized, not just requiring secret (helps with auditing for ex)
        put("/redirect") {
            oAuthApi.updateRedirects(call.receive<ClientDto>())
            call.respond(HttpStatusCode.NoContent)
        }
    }
}
