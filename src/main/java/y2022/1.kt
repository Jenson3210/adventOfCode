package y2022

import utils.printDay
import utils.readFileLineByLineToText
import utils.readFileToText
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream


fun main() {
    printDay(1)
    println(findHeaviestLoadedElves(readFileToText(2022, 1)))

    printDay(2)
    println(findHeaviestLoadedElves(readFileToText(2022, 1), 3))
}

fun findHeaviestLoadedElves(input: String, top: Int = 1): Int {
    return input.split("\n\n").stream()
        .map { it.split("\n") }
        .map { ls -> ls.filter { it.isNotBlank() }.sumOf { it.toInt() } }
        .toList()
        .sortedDescending()
        .take(top)
        .sum()
}
