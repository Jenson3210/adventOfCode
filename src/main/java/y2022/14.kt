package y2022

import utils.groupByFirst
import utils.nonDiagonalLine
import utils.printDay
import utils.readFileLineByLineToText

fun main() {
    printDay(1)
    println(flow(false) {depths -> depths} )

    printDay(2)
    println(flow(true) {depths -> depths + 2})
}

private fun flow(hasFloor: Boolean, bottomCalculator: (Int) -> Int): Long {
    val cave = readFileLineByLineToText(2022, 14)
        .map { it.split(" -> ").map { it.substringAfter(",").toInt() to it.substringBefore(",").toInt() } }
        .flatMap {
            var last: Pair<Int, Int> = Pair(-1, -1)
            it.fold(mutableListOf<Pair<Int, Int>>()) { acc, pair ->
                last = if (acc.isEmpty()) {
                    acc.add(pair)
                    pair
                } else {
                    acc.addAll(last.nonDiagonalLine(pair))
                    pair
                }
                acc
            }.stream()
        }
        .toList()
        .groupByFirst()

    val bottom = bottomCalculator.invoke(cave.keys.max())
    var keepFlowing = true
    var flowCount = 0L
    var flowSand: Boolean
    var currentSandPosition: Pair<Int, Int>
    var nextSandPosition: Pair<Int, Int>?
    while (keepFlowing) {
        flowCount++
        currentSandPosition = 0 to 500
        nextSandPosition = null
        flowSand = true
        while (flowSand) {
            if (nextSandPosition != null) {
                currentSandPosition = nextSandPosition
            }
            nextSandPosition = cave.getNextSandPosition(currentSandPosition)
            if (nextSandPosition == null) {
                cave.getOrPut(currentSandPosition.first){ mutableListOf() }.add(currentSandPosition.second)
                flowSand = false
                if (hasFloor && currentSandPosition.first == 0 && currentSandPosition.second == 500) {
                    keepFlowing = false
                }
            } else if (nextSandPosition.first >= bottom) {
                flowSand = false
                if (hasFloor) {
                    cave.getOrPut(currentSandPosition.first){ mutableListOf() }.add(currentSandPosition.second)
                    if (currentSandPosition.first == 0 && currentSandPosition.second == 500) {
                        keepFlowing = false
                    }
                } else {
                    //last one fell off
                    flowCount --
                    keepFlowing = false
                }
            }
        }
    }
    return flowCount
}

private fun Map<Int, List<Int>>.getNextSandPosition(currentSandPosition: Pair<Int, Int>): Pair<Int, Int>? {
    val takenPositionsOnNextRow = this[currentSandPosition.first + 1]
    if (takenPositionsOnNextRow?.contains(currentSandPosition.second) == true) {
        if (!takenPositionsOnNextRow.contains(currentSandPosition.second -1)) {
            return currentSandPosition.first + 1 to currentSandPosition.second -1
        }
        if (!takenPositionsOnNextRow.contains(currentSandPosition.second +1)) {
            return currentSandPosition.first + 1 to currentSandPosition.second +1
        }
        return null
    } else {
        return currentSandPosition.first + 1 to currentSandPosition.second
    }
}
