package com.example.chess

fun canMove(turno: ColorDePieza, casilla: Casilla): Boolean {
    return if (casilla.isEmpty()) false
    else casilla.pieza!!.colorDePieza == turno
}

fun Tablero.move(inicio: Int, destino: Int): Tablero {
    val allowedMoves = list[inicio].pieza?.allowedMoves(this, inicio)
    if (!isMoveAllowed(inicio, destino, allowedMoves)) return this

    val newList = list.toMutableList()

    val piezaEnInicio = list[inicio].pieza!!
    newList.removeAt(destino)
    newList.add(destino, Casilla(piezaEnInicio.copy(moved=true)))
    newList.removeAt(inicio)
    newList.add(inicio, Casilla(null))

    return Tablero(turno.opponent(), newList)
}

fun Tablero.isMoveAllowed(inicio: Int, destino: Int, allowedMoves: Set<Int>?): Boolean {
    if (inicio == destino) return false
    return allowedMoves
        ?.contains(destino)
        ?: false
}

fun Pieza.allowedMoves(tablero: Tablero, inicio: Int): Set<Int> {
    val casillaEnInicio = tablero.list[inicio]
    val posInicio = Pos(inicio)
    val tipo = casillaEnInicio.pieza!!.tipoDePieza
    val color = casillaEnInicio.pieza.colorDePieza
    val sign = if (color == Blanca) 1 else -1
    var destino: Int
    var casillaDestino: Casilla/* TODO: En passant
    if (tipo == Peon) {
        val allowed = mutableSetOf<Int>()
        // move 1 step
        destino = posInicio.plusRank(1 * sign).toIndex()
        casillaDestino = tablero.list[destino]
        if (casillaDestino.isEmpty()) allowed.add(destino)
        // move 2 steps
        if(!casillaEnInicio.pieza.moved) {
            destino = posInicio.plusRank(2 * sign).toIndex()
            casillaDestino = tablero.list[destino]
            if (casillaDestino.isEmpty()) allowed.add(destino)
        }
        // capture 1
        destino = posInicio.plusRank(1 * sign).plusFile(1).toIndex()
        casillaDestino = tablero.list[destino]
        if (!casillaDestino.isEmpty() && casillaDestino.pieza!!.colorDePieza == color.opponent()) {
            allowed.add(destino)
        }
        // capture 2
        destino = posInicio.plusRank(1 * sign).plusFile(-1).toIndex()
        casillaDestino = tablero.list[destino]
        if (!casillaDestino.isEmpty() && casillaDestino.pieza!!.colorDePieza == color.opponent()) {
            allowed.add(destino)
        }
        // capture 3
        if (posInicio.allowsEnPassant(color)) {
            // TODO: en passsant
        }
        return allowed
    }*/
    return tablero.list.indices.toSet()
}