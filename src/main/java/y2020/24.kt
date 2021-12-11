package y2020

import utils.HexagonalCoordinate
import utils.printDay
import utils.readFileLineByLineToText
import java.util.stream.Stream

fun main() {
    printDay(1)
    print("Black tiles amount: ")
    println(HexagonalFloor().flipTiles(readFileLineByLineToText(2020, 24)).blackTiles.size)

    printDay(2)
    print("After 100 days: ")
    println(HexagonalFloor().flipTiles(readFileLineByLineToText(2020, 24)).passDays(100).blackTiles.size)
}

private class HexagonalFloor {
    var blackTiles = mutableSetOf<HexagonalCoordinate>()

    fun flipTiles(input: Stream<String>) : HexagonalFloor {
        input.forEach { flipTile(it) }
        return this
    }

    private fun flipTile(input: String, coordinate: HexagonalCoordinate = startPoint()) {
        if (input.isNotEmpty()) {
            val move = if (input[0] in listOf('e', 'w')) input.take(1) else input.take(2)
            when (move) {
                "e" -> coordinate.east()
                "w" -> coordinate.west()
                "se" -> coordinate.southEast()
                "sw" -> coordinate.southWest()
                "ne" -> coordinate.northEast()
                "nw" -> coordinate.northWest()
            }
            flipTile(input.substringAfter(move), coordinate)
        } else {
            if (!blackTiles.add(coordinate)) blackTiles.remove(coordinate)
        }
    }

    fun passDays(days: Int): HexagonalFloor {
        repeat(days) {
            anotherDay()
        }
        return this
    }

    private fun anotherDay() {
        val newBlackTiles = mutableSetOf<HexagonalCoordinate>()
        for (tile in blackTiles) {
            val surroundingCoordinates = tile.surroundingCoordinates()
            val surroundingBlackTiles = blackTiles.filter { surroundingCoordinates.contains(it) }
            if (surroundingBlackTiles.size in (1..2)) {
                newBlackTiles.add(tile)
            }
            for (surroundingTile in tile.surroundingCoordinates()) {
                if (blackTiles.filter { surroundingTile.surroundingCoordinates().contains(it) }.size == 2) {
                    newBlackTiles.add(surroundingTile)
                }
            }
        }
        blackTiles = newBlackTiles
    }

    private fun startPoint() : HexagonalCoordinate {
        return HexagonalCoordinate(0, 0, 0)
    }
}
