package com.example.plugins

import com.example.chess.ChessController
import com.example.main.MainHtml
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    val chess = ChessController()
    routing {
        get("/") { MainHtml().html(call) }
        get("/chess") { chess.showBoard(call) }
        get("/chess/move/{inicio}/{destino}") { chess.move(call) }
        get("/chess/promote/{i}/{name}") { chess.promote(call) }
        get("/chess/reset") { chess.reset(call) }
        // Static plugin. Try to access `/static/index.html`
        static("/") {
            resources("static")
        }
        install(StatusPages) {
            exception<AuthenticationException> { cause, _ ->
                cause.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause, _ ->
                cause.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
