package y2022

import utils.commonObjects
import utils.printDay
import utils.readFileLineByLineToText
import java.util.stream.Stream

fun main() {
    printDay(1)
    println(countItemScore(readFileLineByLineToText(2022, 3)))

    printDay(2)
    println(countGroupedItemScore(readFileLineByLineToText(2022, 3)))
}

private fun countItemScore(input: Stream<String>): Int {
    return input.map{ listOf(it.substring(0, (it.length / 2)).toList(), it.substring((it.length / 2)).toList()) }
        .flatMap { it.commonObjects().distinct().stream() as Stream<Char> }
        .mapToInt{ toValue(it) }
        .sum()
}

private fun countGroupedItemScore(input: Stream<String>): Int {
    return input.map{ listOf(it.substring(0, (it.length / 2)).toList(), it.substring((it.length / 2)).toList()) }
        .map { it.flatten() }
        .toList()
        .chunked(3).stream()
        .flatMap { it.commonObjects().distinct().stream() as Stream<Char> }
        .mapToInt{ toValue(it) }
        .sum()
}

private fun toValue(input: Char): Int {
    if (input.isUpperCase()) {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(input) + 27
    }
    return "abcdefghijklmnopqrstuvwxyz".indexOf(input) + 1
}
