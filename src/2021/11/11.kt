package `2021`.`11`

import util.*
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(
        solveDay1(
            readFileLineByLineToText("2021_11.txt").toList()
                .map { it.toList().map { Character.getNumericValue(it) }.map { Octopus(it) } }.toNavigateableMap()
        )
    )

    printDay(2)
    println(
        solveDay2(
            readFileLineByLineToText("2021_11.txt").toList()
                .map { it.toList().map { Character.getNumericValue(it) }.map { Octopus(it) } }.toNavigateableMap()
        )
    )
}

private fun solveDay1(map: NavigateableMap<Octopus>): Long {

    (0 until 100).forEach { _ ->
        map.forEach { it.incrementAndFlash(map) }
        map.forEach { it.reset() }
    }

    return map.cells().sumByLong { it.flashes }
}

private fun solveDay2(map: NavigateableMap<Octopus>): Long {
    var allFlashed = false;
    var counter = 0L
    while (!allFlashed) {
        counter++
        map.forEach { it.incrementAndFlash(map) }
        allFlashed = map.all { it.hasFlashed }
        map.forEach { it.reset() }
    }

    return counter
}

private class Octopus(var value: Int) : PositionAware {
    var flashes: Long = 0
    var hasFlashed = false
    var x: Int? = null
    var y: Int? = null

    fun incrementAndFlash(map: NavigateableMap<Octopus>) {
        value++
        if (shouldFlash() && !hasFlashed) {
            flashes++
            hasFlashed = true
            map.getSurroundingCells(x!!, y!!).forEach { it.incrementAndFlash(map) }
        }
    }

    fun shouldFlash() = value > 9

    fun reset() {
        if (hasFlashed) {
            value = 0
            hasFlashed = false
        }
    }

    override fun setPosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    override fun toString(): String = value.toString()
}