package com.example.Routes

import com.example.Utils.verifyToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    post("/api/v1/auth/sync") {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing token")
            return@post
        }

        val decodedToken = verifyToken(token)
        if (decodedToken == null) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            return@post
        }

        val uid = decodedToken.uid
        val email = decodedToken.email
        val name = decodedToken.name ?: "Anonymous"

        call.respond(HttpStatusCode.OK, mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name
        ))
    }
}
