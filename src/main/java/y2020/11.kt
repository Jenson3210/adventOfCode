package y2020

import utils.*
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Occupied seats after stabilizing waiting area: ")
    println(WaitingArea(readFileLineByLineToText(2020, 11).toList()).getOccupiedSeatsUsingAdjacentSeats())

    printDay(2)
    print("Occupied seats after stabilizing waiting area: ")
    println(WaitingArea(readFileLineByLineToText(2020, 11).toList()).getOccupiedSeatsUsingVisibleSeats())
}

private class WaitingArea(seats : List<String>) {
    var seats: Array<Array<Seat?>>

    init {
        this.seats = Array(size = seats.size) {Array<Seat?>(size = seats.first().length) {null} }
        seats.forEachIndexed { rowIndex, row ->  row.forEachIndexed { seatIndex, seat -> this.seats[rowIndex][seatIndex] = seat.toSeat() } }
    }

    fun getOccupiedSeatsUsingAdjacentSeats() : Int {
        stabilizeWaitingArea(this::getNewStateAdjacacentSeats)
        return seats.map { it.filterNotNull().filter { it.taken }.count() }.sum()
    }

    fun getOccupiedSeatsUsingVisibleSeats() : Int {
        stabilizeWaitingArea(this::getNewStateVisibleSeats)
        return seats.map { it.filterNotNull().filter { it.taken }.count() }.sum()
    }

    private fun stabilizeWaitingArea(seatingRule: ((Int, Int) -> Seat?)) {
        var applying: Int
        do {
            applying = applyRules(seatingRule)
//            println()
//            seats.forEach { println(it.map { it?.toChar() ?: '.' }.toCharArray()) }
        } while (applying != 0)
    }

    private fun applyRules(seatingRule: ((Int, Int) -> Seat?)) : Int {
        val tempSeats = Array(size = seats.size) {Array<Seat?>(size = seats.first().size) {null} }
        var alteredSeats = 0
        var newSeat: Seat?
        seats.forEachIndexed { rowIndex, row -> row.forEachIndexed { seatIndex, seat ->
            newSeat = seatingRule.invoke(rowIndex, seatIndex)
            tempSeats[rowIndex][seatIndex] = newSeat
            if (seat != null && newSeat != null && seat.taken != newSeat!!.taken) {
                alteredSeats ++
            }
        } }
        seats = tempSeats
        return alteredSeats
    }

    private fun getNewStateAdjacacentSeats(rowIndex: Int, seatIndex: Int): Seat? {
        if (seats[rowIndex][seatIndex] == null) {
            return null
        }
        val surroundingSeatsTaken = listOfNotNull(
            seats.northOrNull(rowIndex, seatIndex),
            seats.northEastOrNull(rowIndex, seatIndex),
            seats.eastOrNull(rowIndex, seatIndex),
            seats.southEastOrNull(rowIndex, seatIndex),
            seats.southOrNull(rowIndex, seatIndex),
            seats.southWestOrNull(rowIndex, seatIndex),
            seats.westOrNull(rowIndex, seatIndex),
            seats.northWestOrNull(rowIndex, seatIndex)
        ).filter { it.taken }.count()

        return when (seats[rowIndex][seatIndex]!!.taken) {
            true -> Seat(surroundingSeatsTaken < 4)
            false -> Seat(surroundingSeatsTaken == 0)
        }
    }

    private fun getNewStateVisibleSeats(rowIndex: Int, seatIndex: Int): Seat? {
        if (seats[rowIndex][seatIndex] == null) {
            return null
        }
        val surroundingSeatsTaken = listOfNotNull(
            seats.allNorth(rowIndex, seatIndex).firstOrNull(),
            seats.allNorthEast(rowIndex, seatIndex).firstOrNull(),
            seats.allEast(rowIndex, seatIndex).firstOrNull(),
            seats.allSouthEast(rowIndex, seatIndex).firstOrNull(),
            seats.allSouth(rowIndex, seatIndex).firstOrNull(),
            seats.allSouthWest(rowIndex, seatIndex).firstOrNull(),
            seats.allWest(rowIndex, seatIndex).firstOrNull(),
            seats.allNorthWest(rowIndex, seatIndex).firstOrNull()
        ).filter { it.taken }.count()

        return when (seats[rowIndex][seatIndex]!!.taken) {
            true -> Seat(surroundingSeatsTaken < 5)
            false -> Seat(surroundingSeatsTaken == 0)
        }
    }
}

private class Seat(var taken: Boolean) {
//    fun toChar(): Char {
//        return when(taken) {
//            true -> '#'
//            false -> 'L'
//        }
//    }
}

private fun Char.toSeat() : Seat? {
    return when(this) {
        'L' -> Seat(false)
        '#' -> Seat(true)
        else -> null
    }
}