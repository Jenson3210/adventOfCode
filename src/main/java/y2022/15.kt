package y2022

import utils.printDay
import utils.readFileLineByLineToText
import utils.sumByLong
import kotlin.math.abs

fun main() {
    printDay(1)
    println(countOccupiedPositions())

    printDay(2)
    println(findUnoccupiedPosition())
}

private fun countOccupiedPositions(y: Int = 2000000): Long {
    val sensors = mutableListOf<Sensor>()
    val beacons = mutableListOf<Beacon>()

    readFileLineByLineToText(2022, 15).forEach {
        beacons.add(it.toBeacon())
        sensors.add(it.toSensor())
    }

    val (minX, maxX) = sensors.minOf { it.minX } to sensors.maxOf { it.maxX }

    return (minX..maxX).fold(mutableListOf<Int>()) { acc, x ->
        if (sensors.any { it.coversPoint(x, y) }) {
            acc.add(x)
        }
        acc
    }.filter { x -> sensors.none { it.x == x && it.y == y } && beacons.none { it.x == x && it.y == y }
    }.sumByLong { 1L }
}

private fun findUnoccupiedPosition(searchArea: Int = 4000000): Long {
    val sensors = mutableListOf<Sensor>()
    val beacons = mutableListOf<Beacon>()

    readFileLineByLineToText(2022, 15).forEach {
        beacons.add(it.toBeacon())
        sensors.add(it.toSensor())
    }

    (0..searchArea).forEach { y ->
        var x = 0
        while (x < searchArea) {
            x = sensors.firstOrNull { it.coversPoint(x, y) }?.findEndOfX(y)?: return 1L * x * searchArea + y
        }
    }
    throw Exception("No such point")
}

private data class Beacon(val x: Int, val y: Int)
private data class Sensor(val x: Int, val y: Int, val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    fun coversPoint(x: Int, y: Int): Boolean {
        if (x in minX..maxX && y in minY..maxY) {
            val yDistance = abs(this.y - y)
            return x in (minX + yDistance).. (maxX - yDistance)
        }
        return false
    }

    fun findEndOfX(y: Int): Int = maxX - (abs(this.y - y)) + 1
}

private fun String.toSensor(): Sensor {
    val beacon = this.toBeacon()
    val x = this.getSensorCoordinates().toX()
    val y = this.getSensorCoordinates().toY()
    val distance = abs(beacon.x - x) + abs(beacon.y - y)
    return Sensor(x = x, y = y, minX = x - distance, maxX = x + distance, minY = y - distance, maxY = y + distance)
}
private fun String.getSensorCoordinates(): String =
    this.substringAfter("Sensor at ").substringBefore(": closest beacon is at ")
private fun String.toBeacon(): Beacon = Beacon(this.getBeaconCoordinates().toX(), this.getBeaconCoordinates().toY())
private fun String.getBeaconCoordinates(): String = this.substringAfter(": closest beacon is at ")
private fun String.toX(): Int = this.substringAfter("x=").substringBefore(", ").toInt()
private fun String.toY(): Int = this.substringAfter("y=").substringBefore(":").toInt()
