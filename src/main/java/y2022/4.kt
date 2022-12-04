package y2022

import utils.printDay
import utils.readFileLineByLineToText
import java.util.stream.Stream

fun main() {
    printDay(1)
    println(countFullyOverlappingCleanupAssignments(readFileLineByLineToText(2022, 4)))

    printDay(2)
    println(countPartiallyOverlappingCleanupAssignments(readFileLineByLineToText(2022, 4)))
}

private fun countFullyOverlappingCleanupAssignments(input: Stream<String>): Long {
    return input
        .map { it.split(",") }
        .map { it.map { range -> IntRange(range.split("-").first().toInt(), range.split("-").last().toInt()) } }
        .filter {
            val intersection = it.first().intersect(it.last()).size;
            intersection == it.first().count() || intersection == it.last().count()
        }
        .count()
}

private fun countPartiallyOverlappingCleanupAssignments(input: Stream<String>): Long {
    return input
        .map { it.split(",") }
        .map { it.map { range -> IntRange(range.split("-").first().toInt(), range.split("-").last().toInt()) } }
        .filter { it.first().intersect(it.last()).isNotEmpty() }
        .count()
}
