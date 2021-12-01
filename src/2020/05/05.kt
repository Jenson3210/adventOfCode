package `2020`.`05`

import util.printDay
import util.readFileLineByLineToText
import java.util.stream.Stream
import kotlin.streams.toList

fun main() {
    val plane = Airplane(128, 8)

    printDay(1)
    print("Highest Seat number: ")
    println(plane.getSeatIdStream(readFileLineByLineToText("2020_05.txt")).toList().maxOrNull())

    printDay(2)
    print("Valid passwords: ")
    println(plane.getAllSeatIds().minus(plane.getSeatIdStream(readFileLineByLineToText("2020_05.txt")).toList()))
}

private class Airplane(val amountOfRows: Int, val seatsPerRow: Int) {
    fun getSeatIdStream(tickets: Stream<String>) : Stream<Int> {
        return tickets.map { getSeatId(it) }
    }
    
    fun getAllSeatIds() : List<Int> {
        return IntRange(0, amountOfRows - 1)
            .flatMap { rowIndex -> IntRange(0, seatsPerRow - 1)
                .map { seatIndex -> calculateSeatId(rowIndex, seatIndex) } }
            .toList()
    }

    private fun getSeatId(ticket: String) : Int {
        var rows: List<Int> = IntRange(0, amountOfRows - 1).toList()
        IntRange(0, 6).forEach { rows = getPartOfPlane(ticket[it], rows) }
        val rowNumber: Int = rows[0]

        var seats: List<Int> = IntRange(0, seatsPerRow - 1).toList()
        IntRange(7, 9).forEach { seats = getPartOfPlane(ticket[it], seats) }
        val seatNumber: Int = seats[0]

        return calculateSeatId(rowNumber, seatNumber)
    }

    private fun calculateSeatId(rowNumber: Int, seatNumber: Int) : Int {
        return rowNumber * 8 + seatNumber;
    }

    private fun getPartOfPlane(part: Char, range: List<Int>) : List<Int> {
        val centerNumber = range.size / 2
        if (listOf('B', 'R').contains(part)) {
//            println("keeping ${range.subList(centerNumber, range.size)}")
            return range.subList(centerNumber, range.size)
        }
        if (listOf('F', 'L').contains(part)) {
//            println("keeping ${range.subList(0, centerNumber)}")
            return range.subList(0, centerNumber)
        }
        return emptyList()
    }
}