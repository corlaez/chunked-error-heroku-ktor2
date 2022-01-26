package com.example.chess

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.*
import io.ktor.server.response.*
import kotlinx.html.*

class ChessController {
    private var tablero: Tablero = Tablero()

    suspend fun showBoard(call: ApplicationCall) {
        val indiceSeleccionado = call.parameters["i"]?.toInt()
        val promotionIndex = listOf(0..7, 56..63)
            .map { it.firstOrNull { i -> tablero.list[i].pieza?.tipoDePieza == TipoDePieza.Pawn } }
            .filterNotNull()
            .firstOrNull()
        call.respondHtml(HttpStatusCode.OK) {
            head {
                title { +"Chess Page" }
                meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
                link(rel = "stylesheet", href = "/styles.css", type = "text/css")
            }
            body(classes = "mono center") {
                div(classes = "chessboard-row") {
                    div(classes = "chessboard") {
                        for (i in 0 until tablero.list.size) {
                            //if (i % 8 == 0) br()
                            val casilla = tablero.list[i]
                            var clas = if (casilla.isEmpty()) "vacia" else "ocupada"
                            val iPos = Pos(i)
                            clas += if (iPos.isDark()) " black" else " white"
                            val href = getHref(indiceSeleccionado, casilla, i)
                            div(classes=clas) {
                                if (href == null || promotionIndex != null)
                                    +casilla.toHtmlText()
                                else
                                    a(href=href) { +casilla.toHtmlText() }
                            }
                        }
                    }
                }
                br()
                if (indiceSeleccionado != null) {
                    a(href= "/chess") { +"Soltar Pieza"}
                    br()
                    br()
                }
                if (promotionIndex != null) {// TODO color
                    val posSelected = Pos(promotionIndex)
                    val promotionPiece = tablero.list[promotionIndex].pieza
                    val allowPromotion = promotionPiece?.tipoDePieza == TipoDePieza.Pawn && posSelected.rank == promotionPiece.colorDePieza.promotionRank()
                    if (allowPromotion) {
                        TipoDePieza.promotableList().forEach {
                            a(href= "/chess/promote/$promotionIndex/$it") { +"Promote to $it"}
                            br()
                            br()
                        }
                    }
                }
                br()
                a(href="/chess/reset") { +"reset"}
            }
        }
    }

    suspend fun move(call: ApplicationCall) {
        val inicio = call.parameters["inicio"]!!.toInt()
        val destino = call.parameters["destino"]!!.toInt()
        tablero = tablero.move(inicio, destino)
        // TODO: Log movement event
        call.respondRedirect("/chess", false)
    }

    suspend fun promote(call: ApplicationCall) {
        val promoteToName = call.parameters["name"]!!
        val promoteTo = TipoDePieza.valueOf(promoteToName)
        val promoteToIsAllowed = promoteTo in TipoDePieza.promotableList() // TODO: color
        val index = call.parameters["i"]!!.toInt()
        val currentPiece = tablero.list[index].pieza
        val currentPieceIsPawn = currentPiece?.tipoDePieza == TipoDePieza.Pawn

        if (currentPieceIsPawn && promoteToIsAllowed) {
            val newList = tablero.list.toMutableList()
            newList.removeAt(index)
            newList.add(index, Casilla(Pieza(promoteTo, currentPiece!!.colorDePieza)))
            tablero = tablero.copy(list = newList)
            // TODO: Log promotion event
        }
        call.respondRedirect("/chess", false)
    }

    suspend fun reset(call: ApplicationCall) {
        tablero = Tablero()
        call.respondRedirect("/chess", false)
    }

    private fun Casilla.toHtmlText(): String {
        return pieza?.toString() ?: "*"
    }

    private fun getHref(indiceSeleccionado: Int?, casilla: Casilla, i: Int): String? {
        return if(indiceSeleccionado == null) {
            if (canMove(tablero.turno, casilla)) "/chess?i=$i"
            else null
        } else {
            val allowedMoves = tablero.list[indiceSeleccionado].pieza?.allowedMoves(tablero, indiceSeleccionado)
            if (tablero.isMoveAllowed(indiceSeleccionado, i, allowedMoves)) "/chess/move/$indiceSeleccionado/$i"
            else null
        }
    }
}
