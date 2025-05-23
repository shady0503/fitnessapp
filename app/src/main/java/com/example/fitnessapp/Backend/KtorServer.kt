package com.example.fitnessapp.Backend


import com.example.fitnessapp.Backend.Controllers.UserController
import com.example.fitnessapp.Backend.Repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.gson.*
import com.example.fitnessapp.Backend.Routes.userRoutes
import com.example.fitnessapp.Backend.Utils.initFirebase
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream


object KtorServer {

    private val userRepository = UserRepository()
    private val userController = UserController(userRepository)

    fun start() {
        initFirebase()
        embeddedServer(Netty, port = 8080) {
            install(ContentNegotiation) { gson {} }
            routing {
                get("/") {
                    call.respond(mapOf("message" to "Hello from Ktor Backend!"))
                }
                userRoutes(userController)
            }
        }.start(wait = true)
    }
}


