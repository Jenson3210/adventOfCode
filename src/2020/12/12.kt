package `2020`.`12`

import util.printDay
import util.readFileLineByLineToText
import kotlin.math.abs
import kotlin.streams.toList

fun main() {
    printDay(1)
    print("Manhattan distance between starting point and endpoint: ")
    println(Ship().move(readFileLineByLineToText("2020_12.txt").toList()).getManhattanDistance())

    printDay(2)
    print("Manhattan distance between starting point and endpoint: ")
    println(Ship().moveUsingWaypoint(readFileLineByLineToText("2020_12.txt").toList()).getManhattanDistance())
}


private class Ship(var direction: Direction = Direction.E, var northSouthPosition: Long = 0, var eastWestPosition: Long = 0) {
    fun moveUsingWaypoint(navigations: List<String>, waypointNorthSouthPosition: Long = 1, waypointEastWestPosition: Long = 10): Ship {
        val waypoint = Waypoint(Direction.N.verticalIncrement * waypointNorthSouthPosition, Direction.E.horizontalIncrement * waypointEastWestPosition)
        navigations.forEach { moveWithWaypoint(it, waypoint) }
        return this
    }

    fun move(navigations: List<String>): Ship {
        navigations.forEach { move(it) }
        return this
    }

    private fun move(navigation: String): Ship {
        val directionPart = navigation.take(1)
        val amountPart = navigation.drop(1).toInt()

        when(directionPart) {
            "N", "S" -> northSouthPosition += amountPart * Direction.valueOf(directionPart).verticalIncrement
            "E", "W" -> eastWestPosition += amountPart * Direction.valueOf(directionPart).horizontalIncrement
            "F" -> {
                northSouthPosition += amountPart * direction.verticalIncrement
                eastWestPosition += amountPart * direction.horizontalIncrement
            }
            "L" -> direction = direction.turn(-amountPart)
            "R" -> direction = direction.turn(amountPart)
        }

        return this
    }

    private fun moveWithWaypoint(navigation: String, waypoint: Waypoint): Ship {
        val directionPart = navigation.take(1)
        val amountPart = navigation.drop(1).toInt()

        when(directionPart) {
            "N" -> waypoint.northSouthPosition += amountPart
            "S" -> waypoint.northSouthPosition -= amountPart
            "E" -> waypoint.eastWestPosition += amountPart
            "W" -> waypoint.eastWestPosition -= amountPart
            "F" -> {
                northSouthPosition += amountPart * waypoint.northSouthPosition
                eastWestPosition += amountPart * waypoint.eastWestPosition
            }
            "L" -> waypoint.rotateCounterClockwise(amountPart)
            "R" -> waypoint.rotateClockwise(amountPart)
        }

        return this
    }

    fun getManhattanDistance(): Long = abs(northSouthPosition) + abs(eastWestPosition)
}

private class Waypoint(var northSouthPosition: Long = 0, var eastWestPosition: Long = 0 ) {
    fun rotateClockwise(degrees: Int) {
        repeat(degrees/90) {
            val tempNorthSouth: Long = -eastWestPosition
            eastWestPosition = northSouthPosition
            northSouthPosition = tempNorthSouth
        }
    }

    fun rotateCounterClockwise(degrees: Int) {
        repeat(degrees/90) {
            val tempEastWest: Long = eastWestPosition
            eastWestPosition = -northSouthPosition
            northSouthPosition = tempEastWest
        }
    }
}
private enum class Direction(val verticalIncrement: Int, val horizontalIncrement: Int, var compassDegrees: Int) {
    N(1, 0, 0),
    E(0,1, 90),
    S(-1, 0, 180),
    W(0, -1, 270);

    fun turn(degrees: Int) : Direction {
        var newDegrees = compassDegrees + degrees
        while (newDegrees >= 360) newDegrees -= 360
        while (newDegrees < 0) newDegrees += 360

        return values().first { it.compassDegrees == newDegrees }
    }
}