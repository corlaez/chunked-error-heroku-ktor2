package com.example.chess

enum class TipoDePieza {
    Pawn, Tower, Horse, Bishop, King, Queen;
    fun white(): Pieza { return Pieza(this, Blanca) }
    fun black(): Pieza { return Pieza(this, Negra) }
    companion object {
        fun promotableList(): List<TipoDePieza> { return listOf(Tower, Horse, Bishop, Queen) }
    }
}


sealed class ColorDePieza {
    abstract fun opponent(): ColorDePieza
    abstract fun promotionRank(): Int

}
object Blanca: ColorDePieza() {
    override fun opponent() = Negra
    override fun promotionRank() = 7
}
object Negra: ColorDePieza() {
    override fun opponent() = Blanca
    override fun promotionRank() = 0
}

data class Pieza(val tipoDePieza: TipoDePieza, val colorDePieza: ColorDePieza, val moved: Boolean = false) {
    override fun toString(): String {
        return  when(tipoDePieza) {
            TipoDePieza.Tower -> if (colorDePieza == Blanca) "♖" else "♜"
            TipoDePieza.Horse -> if (colorDePieza == Blanca) "♘" else "♞"
            TipoDePieza.Bishop -> if (colorDePieza == Blanca) "♗" else "♝"
            TipoDePieza.King -> if (colorDePieza == Blanca) "♔" else "♚"
            TipoDePieza.Queen -> if (colorDePieza == Blanca) "♕" else "♛"
            TipoDePieza.Pawn -> if (colorDePieza == Blanca) "♙" else "♟"
        }
    }
}

data class Casilla(val pieza: Pieza?) {
    fun isEmpty(): Boolean {
        return pieza == null
    }
}

val initialSetup = buildList {
    add(Casilla(TipoDePieza.Tower.white()))
    add(Casilla(TipoDePieza.Horse.white()))
    add(Casilla(TipoDePieza.Bishop.white()))
    add(Casilla(TipoDePieza.Queen.white()))
    add(Casilla(TipoDePieza.King.white()))
    add(Casilla(TipoDePieza.Bishop.white()))
    add(Casilla(TipoDePieza.Horse.white()))
    add(Casilla(TipoDePieza.Tower.white()))
    repeat(8) { add(Casilla(TipoDePieza.Pawn.white())) }
    repeat(32) { add(Casilla(null)) }
    repeat(8) { add(Casilla(TipoDePieza.Pawn.black())) }
    add(Casilla(TipoDePieza.Tower.black()))
    add(Casilla(TipoDePieza.Horse.black()))
    add(Casilla(TipoDePieza.Bishop.black()))
    add(Casilla(TipoDePieza.Queen.black()))
    add(Casilla(TipoDePieza.King.black()))
    add(Casilla(TipoDePieza.Bishop.black()))
    add(Casilla(TipoDePieza.Horse.black()))
    add(Casilla(TipoDePieza.Tower.black()))
}

data class Tablero(val turno: ColorDePieza = Blanca, val list: List<Casilla> = initialSetup)

data class Pos(val file: Int, val rank: Int) {
    constructor(origin: Int): this(origin % 8, origin / 8)

    fun toNotation(): String {
        return "${8-rank}${Char(file+97)}"
    }

    fun toIndex(): Int {
        return rank * 8 + file
    }

    fun plusRank(increment: Int): Pos {
        return copy(rank= rank + increment)
    }

    fun plusFile(increment: Int): Pos {
        return copy(file= file + increment)
    }

    fun allowsEnPassant(colorDePieza: ColorDePieza): Boolean {
        return when(colorDePieza) {
            Blanca -> rank == 4
            Negra -> rank == 3
        }
    }

    fun isDark(): Boolean {
        return file % 2 == if (rank % 2 == 0) 1 else 0
    }
}

enum class EventType {
    Move, Promotion
}

sealed interface Event {
    data class MoveEvent(val inicio: Int, val destino: Int): Event
    data class PromotionEvent(val i: Int, val tipoDePieza: TipoDePieza): Event
}
