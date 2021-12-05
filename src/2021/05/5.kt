package `2021`.`05`

import util.printDay
import util.readFileLineByLineToText
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(getPointsDay1())

    printDay(2)
    println(getPointsDay2())
}

private fun getPointsDay1(): Int {
    return readFileLineByLineToText("2021_05.txt")
        .filter { it.matchesDay1Pattern() }
        .flatMap { it.getPointsBetweenCoordinates().stream() }
        .toList()
        .groupingBy { it }
        .eachCount()
        .filter { it.value >= 2 }
        .count()
}

private fun getPointsDay2(): Int {
    return readFileLineByLineToText("2021_05.txt")
        .flatMap { it.getPointsBetweenCoordinates().stream() }
        .toList()
        .groupingBy { it }
        .eachCount()
        .filter { it.value >= 2 }
        .count()
}

private fun String.getPointsBetweenCoordinates(): List<Pair<Int, Int>> {
    val xCoordinates = this.getXCoordinates()
    val yCoordinates = this.getYCoordinates()

    var x = xCoordinates.first
    val xIncrement = xCoordinates.getIncrement()
    var y = yCoordinates.first
    val yIncrement = yCoordinates.getIncrement()
    val coordinates = mutableListOf<Pair<Int, Int>>()

    do {
        coordinates.add(Pair(x, y))
        x += xIncrement
        y += yIncrement
    } while (x != xCoordinates.second || y != yCoordinates.second)
    coordinates.add(Pair(x, y))

    return coordinates.toList()
}

private fun String.matchesDay1Pattern(): Boolean {
    return this.getXCoordinates().toList().distinct().size == 1
            || this.getYCoordinates().toList().distinct().size == 1
}

private fun String.getXCoordinates(): Pair<Int, Int> {
    val values = this.split(" -> ").map { it.substringBefore(",") }.map { it.toInt() }
    return Pair(values[0], values[1])
}

private fun String.getYCoordinates(): Pair<Int, Int> {
    val values = this.split(" -> ").map { it.substringAfter(",") }.map { it.toInt() }
    return Pair(values[0], values[1])
}

private fun Pair<Int, Int>.getIncrement(): Int {
    return if (first == second) 0
    else {
        if (first > second) -1
        else 1
    }
}