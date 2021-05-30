package com.hungry.oauthsample.infrastructure

import com.hungry.oauthsample.api.OAuthApi
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.oauthRoutes(oAuthApi: OAuthApi) {
    route("/user") {
        post {

        }
    }
}