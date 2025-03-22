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
}