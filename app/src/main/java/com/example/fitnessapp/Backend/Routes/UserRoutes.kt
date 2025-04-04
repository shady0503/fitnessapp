package com.example.fitnessapp.Backend.Routes

import com.example.fitnessapp.Backend.Controllers.UserController
import com.example.fitnessapp.Backend.Models.User
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(controller: UserController) {
    route("/users") {
        post("/register") {
            val user = call.receive<User>()
            val result = controller.registerUser(user)
            call.respond(mapOf("status" to result))
        }

        get {
            call.respond(controller.getAllUsers())
        }

        get("/{userId}") {
            val userId = call.parameters["userId"]
            val user = userId?.let { controller.getUserById(it) }
            if (user != null) call.respond(user)
            else call.respond(mapOf("error" to "User not found"))
        }
    }

    post("/api/v1/auth/sync") {
        val token = call.request.headers["Authorization"]?.removePrefix("Bearer ")
        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Missing token")
            return@post
        }

        try {
            val firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val uid = firebaseToken.uid
            val email = firebaseToken.email
            val name = firebaseToken.name

            // Check if user exists in your PostgreSQL DB
            val user = transaction {
                UserEntity.find { Users.email eq email!! }.firstOrNull()
            }

            val finalUser = if (user == null) {
                // Create user
                transaction {
                    UserEntity.new {
                        this.email = email!!
                        this.password = "firebase" // or leave blank
                        this.firstName = name?.split(" ")?.firstOrNull() ?: ""
                        this.lastName = name?.split(" ")?.drop(1)?.joinToString(" ") ?: ""
                    }
                }
            } else {
                user
            }

            call.respond(HttpStatusCode.OK, mapOf("userId" to finalUser.id.value, "email" to finalUser.email))

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Unauthorized, "Invalid token: ${e.message}")
        }
    }

}