package `2021`.`07`

import utils.printDay
import utils.readFileLineCsvToInt
import utils.sumByLong
import kotlin.math.abs
import kotlin.streams.toList

fun main() {
    printDay(1)
    println(getLowestAmountOfFuelNeeded { it.toLong() })

    printDay(2)
    println(getLowestAmountOfFuelNeeded { it -> (0..it).sumByLong { it.toLong() } })
}

private fun getLowestAmountOfFuelNeeded(transform: (Int) -> Long): Long {
    val allNumbers = readFileLineCsvToInt(2021, 7).toList()
    val min = allNumbers.minOrNull()!!
    val max = allNumbers.maxOrNull()!!

    return (min..max).minOf { aligned -> allNumbers.sumOf { transform.invoke(abs(aligned - it)) } }
}