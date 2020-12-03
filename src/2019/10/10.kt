package `2019`.`10`

import util.readFileLineByLineToText
import kotlin.math.*

fun main() {
    val asteroidMap = AsteroidMap()
    readFileLineByLineToText("2019_10.txt").forEach { asteroidMap.addRow(it.toList()) }
    println("A: " + asteroidMap.getHighestVisibleAsteroidCount() + ": " + asteroidMap.getHighestVisibleAsteroid())
    println("B: " + asteroidMap.get200ThElementCount())
}

private class AsteroidMap(val fields: MutableMap<Point, Boolean> = mutableMapOf()) {
    fun addRow(asteroids: List<Char>) {
        val row = fields.keys.map { it.y + 1 }.max() ?: 0
        for (column in asteroids.indices) {
            fields[Point(column, row)] = asteroids[column].toString() == "#"
        }
    }
    fun getHighestVisibleAsteroidCount() = fields.filter { it.value }.keys.map { getVisibleAsteroids(it).size }.max()
    fun getHighestVisibleAsteroid() = fields.filter { it.value }.keys.maxBy { getVisibleAsteroids(it).size }!!

    private fun getVisibleAsteroids(coordinates: Point): MutableList<Pair<Double, MutableList<Point>>> {
        val fieldsCopy: MutableList<Pair<Double, MutableList<Point>>> = mutableListOf()
        fieldsCopy.addAll(
            fields
                .filter { it.key != coordinates && it.value }
                .keys
                .groupBy { coordinates.getAngle(it) }
                .map { entry -> entry.key to entry.value.sortedBy { coordinates.getDistance(it) }.toMutableList() }
                .sortedBy { it.first }
                .toMutableList())
        return fieldsCopy
    }

    fun get200ThElementCount() : Int {
        var toReturn = Point(0, 0)
        val asteroids = getVisibleAsteroids(getHighestVisibleAsteroid())
        for (index in 0..199) {
            if (asteroids[index.rem(asteroids.size)].second.isNotEmpty()) {
                toReturn = asteroids[index.rem(asteroids.size)].second.removeAt(0)
            }
        }
        return (100*toReturn.x) + toReturn.y
    }

}

private class Point(val x: Int, val y: Int) {
    fun getAngle(target: Point): Double {
        var angle = Math.toDegrees(atan2( target.y - y.toDouble(), target.x - x.toDouble())) + 90
        if (angle < 0) {
            angle += 360
        }
        return angle
    }

    fun getDistance(target: Point): Int {
        return sqrt((target.x - x.toDouble()).pow(2) + (target.y - y.toDouble()).pow(2)).toInt()
    }

    override fun toString(): String = "Point($x:$y)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }


}