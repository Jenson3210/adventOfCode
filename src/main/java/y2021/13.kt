package y2021

import printPairOfOrColsAndRows
import utils.printDay
import utils.readFileLineByLineToText
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(foldSingleTime())

    printDay(2)
    fold()
}

private fun foldSingleTime(): Int {
    val list: MutableList<String> = mutableListOf()
    var encounteredBlank = false
    readFileLineByLineToText(2021, 13).toList()
        .forEach {
            if (encounteredBlank) {
                list.add(it)
            } else if (it.isEmpty()) {
                encounteredBlank = true
            }
        }

    return fold(buildMap(), list[0]).size
}

private fun fold() {
    var map = buildMap()
    val list: MutableList<String> = mutableListOf()
    var encounteredBlank = false
    readFileLineByLineToText(2021, 13).toList()
        .forEach {
            if (encounteredBlank) {
                list.add(it)
            } else if (it.isEmpty()) {
                encounteredBlank = true
            }
        }

    list.forEach { map = fold(map, it) }

    map.printPairOfOrColsAndRows()
}

private fun fold(map: Set<Pair<Int, Int>>, instruction: String): Set<Pair<Int, Int>> {
    return map.map {
        if (it.getValue(instruction) > instruction.getFoldLine()) {
            val diff = it.getValue(instruction) - instruction.getFoldLine()
            if (instruction.isXFold()) {
                (instruction.getFoldLine() - diff to it.second)
            } else (it.first to instruction.getFoldLine() - diff)
        } else {
            it
        }
    }.toSet()
}

private fun Pair<Int, Int>.getValue(instruction: String): Int {
    return if (instruction.isXFold()) {
        this.first
    } else {
        this.second
    }
}

private fun String.isXFold(): Boolean {
    return this.substringAfter("fold along ").startsWith("x")
}

private fun String.getFoldLine(): Int {
    return this.substringAfter("=").toInt()
}

private fun buildMap(): Set<Pair<Int, Int>> {
    return readFileLineByLineToText(2021, 13).toList()
        .takeWhile { it.isNotEmpty() }
        .map { (it.substringBefore(",").toInt() to it.substringAfter(",").toInt()) }
        .toSet()
}