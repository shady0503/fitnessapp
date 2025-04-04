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


object KtorServer {

    private val userRepository = UserRepository()
    private val userController = UserController(userRepository)

    fun start() {
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

fun initFirebase() {
    val serviceAccount = FileInputStream("/app/google-services.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
}
