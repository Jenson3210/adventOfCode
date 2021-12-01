package `2021`.`01`

import util.printDay
import util.readFileLineByLineToInt
import kotlin.streams.toList


fun main() {
    printDay(1)
    println(getIncreasedCountOfPairs(readFileLineByLineToInt("2021_01.txt").toList()))

    printDay(2)
    println(getIncreasedCountOfMovingWindows(readFileLineByLineToInt("2021_01.txt").toList()))
}

fun getIncreasedCountOfPairs(values: List<Int>): Long {
    var increasedCount = 0L
    var current: Int? = null
    var previous: Int?
    val it = values.listIterator()
    while (it.hasNext()) {
        previous = current
        current = it.next()
        if (previous != null && current > previous) {
            increasedCount++
        }
    }

    return increasedCount
}

fun getIncreasedCountOfMovingWindows(values: List<Int>): Long {
    var increasedCount = 0L
    var first: Int?
    var second: Int? = null
    var third: Int? = null
    var current: Int? = null
    var previous: Int?
    val it = values.listIterator()
    while (it.hasNext()) {
        previous = current
        first = second
        second = third
        third = it.next()
        if (first != null && second != null) {
            current = first + second + third
            if (previous != null && current > previous) {
                increasedCount++
            }
        }
    }

    return increasedCount
}