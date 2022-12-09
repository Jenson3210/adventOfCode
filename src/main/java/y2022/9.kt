package y2022

import utils.printDay
import utils.readFileLineByLineToText
import java.util.stream.Stream
import kotlin.math.abs

fun main() {
    printDay(1)
    println(traverse(readFileLineByLineToText(2022, 9)))

    printDay(2)
    println(traverse(readFileLineByLineToText(2022, 9), 9))
}

private fun traverse(moves: Stream<String>, times: Int = 1): Int {
    var head = 0 to 0
    val positions = mutableListOf(head)

    moves.forEach {
        (1..it.substringAfter(" ").toInt()).forEach { _ ->
            head = when (it.substringBefore(" ")) {
                "R" -> head.first to head.second + 1
                "U" -> head.first - 1 to head.second
                "L" -> head.first to head.second - 1
                "D" -> head.first + 1 to head.second
                else -> throw Exception()
            }
            positions.add(head)
        }
    }
    return tail(positions, times)
}

private fun tail(headPositions: List<Pair<Int, Int>>, times: Int, count: Int = 1): Int {
    val visitedPositions = mutableListOf<Pair<Int, Int>>()
    var tail = 0 to 0
    headPositions.forEach { head ->
        //should move up or down
        if (abs(head.first - tail.first) > 1) {
            tail = when (head.second.compareTo(tail.second)) {
                //should move diagonally left up/down
                -1 -> tail.first + head.first.compareTo(tail.first) to tail.second - 1
                //simply up/down
                0 -> tail.first + head.first.compareTo(tail.first) to tail.second
                //should move diagonally right up/down
                1 -> tail.first + head.first.compareTo(tail.first) to tail.second + 1
                else -> throw Exception()
            }
        } else if (abs(head.second - tail.second) > 1) {
            tail = when (head.first.compareTo(tail.first)) {
                //should move diagonally up left/right
                -1 -> tail.first - 1 to tail.second + head.second.compareTo(tail.second)
                //simply left/right
                0 -> tail.first to tail.second + head.second.compareTo(tail.second)
                //should move diagonally down left/right
                1 -> tail.first + 1 to tail.second + head.second.compareTo(tail.second)
                else -> throw Exception()
            }
        }
        visitedPositions.add(tail)
    }
//    println()
//    visitedPositions.printPairOfOrColsAndRows()
    return if (times == count) {
        visitedPositions.distinct().count()
    } else {
        tail(visitedPositions, times, count + 1)
    }
}
