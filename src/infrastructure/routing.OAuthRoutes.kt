package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.api.OAuthApi
import com.hungry.oauthsample.api.dto.`in`.CreateUserDto
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.oauthRoutes(oAuthApi: OAuthApi) {
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
    }
}