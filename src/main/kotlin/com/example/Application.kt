package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        configureRouting()
        configureSecurity()
        configureMonitoring()
        configureSerialization()
        configureSockets()
    }.start(wait = true)
}
