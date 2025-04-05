package com.example.fitnessapp.Backend.Routes

import com.example.fitnessapp.Backend.Controllers.UserController
import com.example.fitnessapp.Backend.Models.UserEntity
import com.example.fitnessapp.Backend.Models.Users
import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.userRoutes(controller: UserController) {
    route("/users") {
        post("/register") {
            // Receive the data as UserEntity
            val user = call.receive<UserEntity>()
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
            // Verify Firebase ID token
            val firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val uid = firebaseToken.uid
            val email = firebaseToken.email
            val name = firebaseToken.name

            // Check if the user exists in PostgreSQL database
            val user = transaction {
                UserEntity.find { Users.email eq email!! }.firstOrNull() // Ensure UserEntity is used here
            }

            val finalUser = if (user == null) {
                // User does not exist, create a new user
                transaction {
                    UserEntity.new {
                        this.email = email
                        this.password = "firebase" // You can leave it blank or store a default value
                        this.firstName = name?.split(" ")?.firstOrNull() ?: ""
                        this.lastName = name?.split(" ")?.drop(1)?.joinToString(" ") ?: ""
                    }
                }
            } else {
                // User exists, return the existing user
                user
            }

            call.respond(HttpStatusCode.OK, mapOf("userId" to finalUser.id.value, "email" to finalUser.email))

        } catch (e: Exception) {
            // Handle any exceptions, such as invalid token or database errors
            call.respond(HttpStatusCode.Unauthorized, "Invalid token: ${e.message}")
        }
    }
}
