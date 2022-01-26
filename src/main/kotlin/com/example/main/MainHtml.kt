package com.example.main

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.*
import kotlinx.html.*

class MainHtml {
    suspend fun html(call: ApplicationCall) {
        call.respondHtml(HttpStatusCode.OK) {
            head {
                title { +"Main Page" }
            }
            body {
                p { +"Hello World!" }
                a(href = "  chess") { +"chess" }
            }
        }
    }
}
