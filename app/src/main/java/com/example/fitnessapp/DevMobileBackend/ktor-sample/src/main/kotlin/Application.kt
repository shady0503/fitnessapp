package com.example

import com.example.DatabaseConfig.initSupabase
import com.example.Utils.initFirebase
import io.ktor.server.application.*

fun main(args: Array<String>) {
    //initFirebase()
    initSupabase()
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //configureSecurity()
    configureRouting()
}
